package com.veloMTL.veloMTL.Service.History;


import com.veloMTL.veloMTL.DTO.History.TripHistoryDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.User;
import com.veloMTL.veloMTL.Repository.PRC.BillingRepository;
import com.veloMTL.veloMTL.Repository.BMSCore.TripRepository;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HistoryService {
    @Autowired
    private RiderRepository riderRepository;
    private OperatorRepository operatorRepository;
    private TripRepository tripRepository;
    private BillingRepository billingRepository;

    public HistoryService(RiderRepository riderRepository, TripRepository tripRepository, OperatorRepository operatorRepository, BillingRepository billingRepository) {
        this.riderRepository = riderRepository;
        this.tripRepository = tripRepository;
        this.operatorRepository = operatorRepository;
        this.billingRepository = billingRepository;
    }

    //Fetches Current Session's User Email from SecurityContextHolder
    public User fetchCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("USER NOT AUTHENTICATED");
        }
        String email = auth.getName();
        User currentUser = null;
        currentUser = operatorRepository.findByEmail(email).orElse(null);
        if (currentUser != null) {
            return currentUser;
        }
        currentUser = riderRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User Not Found"));
        return currentUser;
    }

    public List<TripHistoryDTO> processTrips(List<Trip> trips) {
        return trips.stream().map(TripHistoryDTO::new).toList();
    }

    public List<TripHistoryDTO> fetchRiderTrips(String userID) {
        // Convert userID (could be ID or email) to email for querying
        User user = null;
        // Try to find as Rider by ID
        user = riderRepository.findById(userID).orElse(null);
        if (user == null) {
            // Try to find as Operator by ID
            user = operatorRepository.findById(userID).orElse(null);
        }
        if (user == null) {
            // Try to find as Rider by email
            user = riderRepository.findByEmail(userID).orElse(null);
        }
        if (user == null) {
            // Try to find as Operator by email
            user = operatorRepository.findByEmail(userID).orElse(null);
        }
        if (user == null) {
            throw new RuntimeException("User not found with ID or email: " + userID);
        }
        
        // Query trips by user email
        List<Trip> filteredTrips = tripRepository.fetchTripsByUserId(user.getEmail());
        return processTrips(filteredTrips);
    }

    public List<TripHistoryDTO> fetchAllTrips() {
        return processTrips(tripRepository.findAll());
    }
}
