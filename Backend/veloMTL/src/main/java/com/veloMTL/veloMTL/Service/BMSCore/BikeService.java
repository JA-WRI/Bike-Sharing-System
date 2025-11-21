package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Enums.*;
import com.veloMTL.veloMTL.PCR.Billing;
import com.veloMTL.veloMTL.Service.PRC.BillingService;
import com.veloMTL.veloMTL.Patterns.State.Bikes.*;
import com.veloMTL.veloMTL.Repository.BMSCore.BikeRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import com.veloMTL.veloMTL.utils.Mappers.BikeMapper;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class BikeService {
    private final BikeRepository bikeRepository;
    private final DockRepository dockRepository;
    private final StationRepository stationRepository;
    private final StationService stationService;
    private final TripService tripService;
    private final TimerService timerService;
    private final BillingService billingService;

    public static final int EXPIRY_TIME_MINS = 15;

    public BikeService(BikeRepository bikeRepository, DockRepository dockRepository,
                       StationRepository stationRepository, StationService stationService, TripService tripService,
                       TimerService timerService, RiderRepository riderRepository, BillingService billingService) {
        this.bikeRepository = bikeRepository;
        this.dockRepository = dockRepository;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.tripService = tripService;
        this.timerService = timerService;
        this.billingService = billingService;
    }

    //can change this later if needed
    public BikeDTO createBike(BikeDTO bikeDTO){
        //Find the dock
        Dock dock = dockRepository.findById(bikeDTO.getDockId()).orElseThrow(() -> new RuntimeException("Dock not found"));;
        Bike bike = BikeMapper.dtoToEntity(bikeDTO, dock);
        Station station = dock.getStation();

        //save the bike
        bikeRepository.save(bike);
        //update the dock status
        dock.setStatus(DockStatus.OCCUPIED);
        //assign the bike to the dock
        dock.setBike(bike.getBikeId());
        //save the dock
        dockRepository.save(dock);
        //change the station status
        station.setStationStatus(StationStatus.OCCUPIED);
        //change station occupancy
        int newOccupancy = station.getOccupancy()+1;
        stationService.updateStationOccupancy(station, newOccupancy);

        //save the station
        stationRepository.save(dock.getStation());
        return BikeMapper.entityToDto(bike);
    }

    public ResponseDTO<BikeDTO> unlockBike(String bikeId, String userId, UserStatus role){
        Bike bike = loadDockWithState(bikeId);
        Dock dock = bike.getDock();

        StateChangeResponse message = bike.getState().unlockBike(bike, dock, role, LocalDateTime.now(), userId);
        if (dock == null) {
            return new ResponseDTO<>(message.getStatus(), message.getMessage(), BikeMapper.entityToDto(bike));
        }
        Station station = dock.getStation();

        //only do this if we were able to perform the command
        if(message.isSuccess()) {
            //update station occupancy
            int newOccupancy = station.getOccupancy() - 1;
            stationService.updateStationOccupancy(station, newOccupancy);

            dock.setBike(null);
            bike.setDock(null);

            Bike savedBike = bikeRepository.save(bike);
            dockRepository.save(dock);
            stationRepository.save(station);

            //if user is a rider then we create them a trip
            if (role == UserStatus.RIDER) {
                tripService.createTrip(bikeId, userId, station);
                tripService.endReserveTripIfExists(bikeId, userId);
                return new ResponseDTO<>(message.getStatus(), message.getMessage(), BikeMapper.entityToDto(savedBike));
            }
        }

        return new ResponseDTO<>(message.getStatus(), message.getMessage(), BikeMapper.entityToDto(bike));
    }

    public ResponseDTO<BikeDTO> lockBike(String bikeId, String userId, String dockId, UserStatus role){
        Bike bike = loadDockWithState(bikeId);
        Dock dock = dockRepository.findById(dockId).orElseThrow(() -> new RuntimeException("Dock not found with ID: " + dockId));
        Station station = dock.getStation();
        StateChangeResponse message = bike.getState().lockBike(bike, dock, role);

        //only do this if successful
        if (message.getStatus()== StateChangeStatus.SUCCESS) {

            //setting bike and dock
            bike.setDock(dock);
            dock.setBike(bike.getBikeId());

            //update station occupancy
            int newOccupancy = station.getOccupancy() + 1;
            stationService.updateStationOccupancy(station, newOccupancy);

            //saving to database
            Bike savedBike = bikeRepository.save(bike);
            dockRepository.save(dock);
            stationRepository.save(station);

            if(role == UserStatus.RIDER){
                //call end trip
                Trip trip = tripService.findOngoingTrip(bikeId, userId);

                if (trip != null) {
                    String arrivalStation = station.getStationName();
                    trip.setEndTime(LocalDateTime.now());
                    trip.setArrivalStation(arrivalStation);
                    Billing bill = billingService.pay(trip);
                    tripService.endTrip(trip, station, bill);
                }
            }
            return new ResponseDTO<>(message.getStatus(), message.getMessage(), BikeMapper.entityToDto(savedBike));
        }
        return new ResponseDTO<>(message.getStatus(), message.getMessage(), BikeMapper.entityToDto(bike));
    }

    public ResponseDTO<BikeDTO> reserveBike(String bikeId, String username, String dockId, LocalDateTime reserveDate, UserStatus role) {
        // Check if user already has existing reservation
        List<Bike> reservedBikes = bikeRepository.findByReserveUser(username);
        if (!reservedBikes.isEmpty()) throw new RuntimeException("Existing reservation for bike with ID: " + reservedBikes.getFirst().getBikeId());

        Bike bike = loadDockWithState(bikeId);
        Dock dock = dockRepository.findById(dockId).orElseThrow(() -> new RuntimeException("Dock not found with ID: " + dockId));

        StateChangeResponse message = bike.getState().reserveBike(bike, dock, reserveDate, username);
        Bike savedBike = bikeRepository.save(bike);
        //Create Reserve Trip
        tripService.createReserveTrip(bikeId, username, bike.getDock().getStation());
//        Create Reservation


        long expiryTimeMs = EXPIRY_TIME_MINS*60*1000;
        long latencyDelayMs = 1000;
        timerService.scheduleReservationExpiry(bikeId, username, expiryTimeMs + latencyDelayMs, () -> {
            Bike reservedBike = loadDockWithState(bikeId);

            LocalDateTime now = LocalDateTime.now();
            if (reservedBike.getReserveDate() != null && now.isAfter(reservedBike.getReserveDate().plusSeconds(4))) {
                expireReservation(bikeId, username, role);
                tripService.expireReservation(bikeId, username);
            }
        });

        return new ResponseDTO<>(message.getStatus(), message.getMessage(), BikeMapper.entityToDto(savedBike));
    }

    public ResponseDTO<BikeDTO> expireReservation(String bikeId, String userId, UserStatus userStatus) {
        List<Bike> reservedBikes = bikeRepository.findByReserveUser(userId);
        if (reservedBikes.isEmpty()) throw new RuntimeException("No existing reservations found.");

        Bike bike = loadDockWithState(reservedBikes.getFirst().getBikeId());
        StateChangeResponse message = bike.getState().lockBike(bike, bike.getDock(), userStatus);
        Bike savedBike = bikeRepository.save(bike);

        return new ResponseDTO<>(message.getStatus(), message.getMessage(), BikeMapper.entityToDto(savedBike));
    }

    //Helper methods
    Bike loadDockWithState(String bikeId) {
        Bike bike = bikeRepository.findById(bikeId)
                .orElseThrow(() -> new RuntimeException("Bike not found with ID: " + bikeId));
        bike.setState(createStateFromStatus(bike.getBikeStatus()));
        return bike;
    }

    private BikeState createStateFromStatus(BikeStatus status) {
        return switch (status) {
            case AVAILABLE -> new AvailableBikeState();
            case RESERVED -> new ReservedBikeState();
            case ON_TRIP -> new OnTripBikeState();
            case OUT_OF_SERVICE -> new MaintenanceBikeState();
        };
    }

    public BikeDTO getBikeById(String bikeId) {
        Bike bike = bikeRepository.findById(bikeId)
                .orElseThrow(() -> new RuntimeException("Bike not found with ID: " + bikeId));
        return BikeMapper.entityToDto(bike);
    }

    public List<BikeDTO> getBikesByStation(String stationId) {
        // Get all bikes and filter by station
        List<Bike> allBikes = bikeRepository.findAll();
        List<BikeDTO> stationBikes = new java.util.ArrayList<>();
        
        for (Bike bike : allBikes) {
            try {
                // Get dockId from bike (handles lazy loading)
                String bikeDockId = null;
                try {
                    if (bike.getDock() != null) {
                        bikeDockId = bike.getDock().getDockId();
                    }
                } catch (Exception e) {
                    BikeDTO tempDto = BikeMapper.entityToDto(bike);
                    bikeDockId = tempDto.getDockId();
                }
                
                // Check if bike's dock belongs to this station (dockId starts with stationId-)
                if (bikeDockId != null && bikeDockId.startsWith(stationId + "-")) {
                    BikeDTO dto = BikeMapper.entityToDto(bike, bikeDockId);
                    stationBikes.add(dto);
                }
            } catch (Exception e) {
                // Log error but continue processing other bikes
                System.err.println("Error processing bike " + bike.getBikeId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        return stationBikes;
    }
}
