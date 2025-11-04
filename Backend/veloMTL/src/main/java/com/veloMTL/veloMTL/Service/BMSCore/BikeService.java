package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Enums.*;
import com.veloMTL.veloMTL.Patterns.State.Bikes.*;
import com.veloMTL.veloMTL.Repository.BMSCore.BikeRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import com.veloMTL.veloMTL.utils.Mappers.BikeMapper;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class BikeService {
    private final BikeRepository bikeRepository;
    private final DockRepository dockRepository;
    private final StationRepository stationRepository;
    private final StationService stationService;
    private final TripService tripService;

    public BikeService(BikeRepository bikeRepository, DockRepository dockRepository, StationRepository stationRepository, StationService stationService, TripService tripService, RiderRepository riderRepository) {
        this.bikeRepository = bikeRepository;
        this.dockRepository = dockRepository;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.tripService = tripService;
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
                tripService.createTrip(bikeId, userId);
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
                    tripService.endTrip(trip);
                }
            }
            return new ResponseDTO<>(message.getStatus(), message.getMessage(), BikeMapper.entityToDto(savedBike));
        }
        return new ResponseDTO<>(message.getStatus(), message.getMessage(), BikeMapper.entityToDto(bike));
    }

    public ResponseDTO<BikeDTO> reserveBike(String bikeId, String username, String dockId, LocalDateTime reserveDate, UserStatus role) {
        Bike bike = loadDockWithState(bikeId);
        Dock dock = dockRepository.findById(dockId).orElseThrow(() -> new RuntimeException("Dock not found with ID: " + dockId));

        StateChangeResponse message = bike.getState().reserveBike(bike, dock, reserveDate, username);
        Bike savedBike = bikeRepository.save(bike);

        return new ResponseDTO<>(message.getStatus(), message.getMessage(), BikeMapper.entityToDto(savedBike));
    }

    //Helper methods
    private Bike loadDockWithState(String bikeId) {
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
}
