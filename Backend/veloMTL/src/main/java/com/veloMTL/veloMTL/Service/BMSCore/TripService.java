package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
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
    private final RiderRepository riderRepository;
    private final TripRepository tripRepository;

    public TripService(BikeRepository bikeRepository, RiderRepository riderRepository, TripRepository tripRepository) {
        this.bikeRepository = bikeRepository;
        this.riderRepository = riderRepository;
        this.tripRepository = tripRepository;
    }

    //can change this later if needed
    public Trip createTrip(String bikeId, String riderId, Station station) {
        //Find the bike
        Bike bike = bikeRepository.findById(bikeId).orElseThrow(() -> new RuntimeException("Bike not found"));;
        //Find the rider
        Rider rider = riderRepository.findById(riderId).orElseThrow(() -> new RuntimeException("Rider not found"));;

        // Create Trip object
        String originStation = station.getStationName();

        Trip trip = new Trip(bike, rider);
        trip.setStartTime(LocalDateTime.now());
        trip.setOriginStation(originStation);
        //save the trip
        Trip savedTrip = tripRepository.save(trip);
        riderRepository.save(rider);

        return trip;
    }

    public TripDTO endTrip(Trip trip, Station station) {
        String arrivalStation = station.getStationName();
        trip.setEndTime(LocalDateTime.now());
        trip.setArrivalStation(arrivalStation);
        Trip savedTrip = tripRepository.save(trip);

        return TripMapper.entityToDto(savedTrip);
    }

    public Trip findOngoingTrip(String bikeId, String riderId) {
        return tripRepository.findOngoingTrip(bikeId, riderId);
    }
}
