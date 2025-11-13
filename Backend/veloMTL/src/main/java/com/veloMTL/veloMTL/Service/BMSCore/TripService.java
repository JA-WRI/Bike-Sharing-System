package com.veloMTL.veloMTL.Service.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.PCR.Billing;
import com.veloMTL.veloMTL.Repository.BMSCore.BikeRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.DockRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.StationRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.TripRepository;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import com.veloMTL.veloMTL.utils.Mappers.TripMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class TripService {
    private final BikeRepository bikeRepository;
    private final RiderRepository riderRepository;
    private final OperatorRepository operatorRepository;
    private final TripRepository tripRepository;

    public TripService(BikeRepository bikeRepository, RiderRepository riderRepository, 
                       OperatorRepository operatorRepository, TripRepository tripRepository) {
        this.bikeRepository = bikeRepository;
        this.riderRepository = riderRepository;
        this.operatorRepository = operatorRepository;
        this.tripRepository = tripRepository;
    }

    /**
     * Helper class to hold either a Rider or Operator reference
     */
    private static class UserReference {
        final Rider rider;
        final Operator operator;
        
        UserReference(Rider rider) {
            this.rider = rider;
            this.operator = null;
        }
        
        UserReference(Operator operator) {
            this.rider = null;
            this.operator = operator;
        }
        
        String getId() {
            return rider != null ? rider.getId() : operator.getId();
        }
    }
    
    /**
     * Finds a user (Rider or Operator) by ID or email.
     * 
     * @param userId The user ID or email
     * @return UserReference containing either Rider or Operator
     */
    private UserReference findUser(String userId) {
        // First, try to find as Rider by ID
        Rider rider = riderRepository.findById(userId).orElse(null);
        if (rider != null) {
            return new UserReference(rider);
        }
        
        // Try to find as Rider by email
        rider = riderRepository.findByEmail(userId).orElse(null);
        if (rider != null) {
            return new UserReference(rider);
        }
        
        // Try to find as Operator by ID
        Operator operator = operatorRepository.findById(userId).orElse(null);
        if (operator != null) {
            return new UserReference(operator);
        }
        
        // Try to find as Operator by email
        operator = operatorRepository.findByEmail(userId).orElse(null);
        if (operator != null) {
            return new UserReference(operator);
        }
        
        throw new RuntimeException("User not found with ID or email: " + userId);
    }

    //can change this later if needed
    public Trip createTrip(String bikeId, String userId, Station station) {
        //Find the bike
        Bike bike = bikeRepository.findById(bikeId).orElseThrow(() -> new RuntimeException("Bike not found"));
        //Find user (Rider or Operator)
        UserReference userRef = findUser(userId);

        // Create Trip object
        String originStation = station.getStationName();

        Trip trip;
        if (userRef.rider != null) {
            trip = new Trip(bike, userRef.rider);
        } else {
            trip = new Trip(bike, userRef.operator);
        }
        trip.setStartTime(LocalDateTime.now());
        trip.setOriginStation(originStation);
        //save the trip
        Trip savedTrip = tripRepository.save(trip);
        
        // Save the user entity to ensure it's persisted
        if (userRef.rider != null) {
            riderRepository.save(userRef.rider);
        } else {
            operatorRepository.save(userRef.operator);
        }

        return savedTrip;
    }

    public TripDTO endTrip(Trip trip, Station station, Billing billing) {
        trip.setBilling(billing);
        Trip savedTrip = tripRepository.save(trip);

        return TripMapper.entityToDto(savedTrip);
    }

    public Trip findOngoingTrip(String bikeId, String userId) {
        // Find user to get the correct ID
        UserReference userRef = findUser(userId);
        String userDbId = userRef.getId();
        
        // Try to find trip with rider reference
        Trip trip = tripRepository.findOngoingTripByRider(bikeId, userDbId);
        if (trip != null) {
            return trip;
        }
        
        // Try to find trip with operator reference
        trip = tripRepository.findOngoingTripByOperator(bikeId, userDbId);
        return trip;
    }

    public Trip createReserveTrip(String bikeId, String userId, Station station) {
        //Find the bike
        Bike bike = bikeRepository.findById(bikeId).orElseThrow(() -> new RuntimeException("Bike not found"));
        //Find user (Rider or Operator)
        UserReference userRef = findUser(userId);
        String originStation = station.getStationName();
        
        Trip trip;
        if (userRef.rider != null) {
            trip = new Trip(bike, userRef.rider);
        } else {
            trip = new Trip(bike, userRef.operator);
        }
        trip.setOriginStation(originStation);
        Trip savedTrip = tripRepository.save(trip);
        
        // Save the user entity to ensure it's persisted
        if (userRef.rider != null) {
            riderRepository.save(userRef.rider);
        } else {
            operatorRepository.save(userRef.operator);
        }
        
        return savedTrip;
    }

    public Trip startReserveTrip(Trip trip) {
        //Find the bike
        Bike bike = bikeRepository.findById(trip.getBike().getBikeId()).orElseThrow(() -> new RuntimeException("Bike not found"));
        //The user (rider or operator) should already be set in the trip, but verify it exists
        if (trip.getUserId() == null) {
            throw new RuntimeException("User not found in trip");
        }
        
        // Verify the user still exists in the database
        UserReference userRef = findUser(trip.getUserId());
        if (userRef.rider != null) {
            trip.setRider(userRef.rider);
        } else {
            trip.setOperator(userRef.operator);
        }
        
        trip.setStartTime(LocalDateTime.now());
        //save the trip
        Trip savedTrip = tripRepository.save(trip);
        return savedTrip;
    }
}
