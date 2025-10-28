package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.BMSCore.BikeRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.TripRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import com.veloMTL.veloMTL.utils.Mappers.TripMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class TripService {
    private final BikeRepository bikeRepository;
    private final DockRepository dockRepository;
    private final StationRepository stationRepository;
    private final StationService stationService;
    private final RiderRepository riderRepository;
    private final TripRepository tripRepository;

    public TripService(BikeRepository bikeRepository, DockRepository dockRepository, StationRepository stationRepository, StationService stationService, RiderRepository riderRepository, TripRepository tripRepository) {
        this.bikeRepository = bikeRepository;
        this.dockRepository = dockRepository;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
        this.riderRepository = riderRepository;
        this.tripRepository = tripRepository;
    }

    //can change this later if needed
    public TripDTO createTrip(TripDTO tripDTO) {
        //Find the bike
        Bike bike = bikeRepository.findById(tripDTO.getBikeId()).orElseThrow(() -> new RuntimeException("Bike not found"));;
        //Find the rider
        Rider rider = riderRepository.findById(tripDTO.getRiderId()).orElseThrow(() -> new RuntimeException("Rider not found"));;

        // Create Trip object
        Trip trip = TripMapper.dtoToEntity(tripDTO, bike, rider);
        LocalDateTime startTime = LocalDateTime.now();
        trip.setStartTime(startTime);

        //save the trip
        Trip savedTrip = tripRepository.save(trip);

        return TripMapper.entityToDto(savedTrip);
    }

    public TripDTO endTrip(Trip trip) {
        LocalDateTime endTime = LocalDateTime.now();
        Trip endedTrip = new Trip(trip.getTripId(), trip.getStartTime(), endTime, trip.getBike(), trip.getRider());

        Trip savedTrip = tripRepository.save(endedTrip);

        return TripMapper.entityToDto(savedTrip);
    }

    public Trip findOngoingTrip(String bikeId, String riderId) {
        return tripRepository.findOngoingTrip(bikeId, riderId);
    }
}
