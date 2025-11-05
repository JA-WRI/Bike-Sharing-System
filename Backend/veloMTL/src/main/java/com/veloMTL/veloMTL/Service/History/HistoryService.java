package com.veloMTL.veloMTL.Service.History;


import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.BMSCore.TripRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class HistoryService {
    @Autowired
    private RiderRepository riderRepository;
    private TripRepository tripRepository;

    public HistoryService(RiderRepository riderRepository, TripRepository tripRepository) {
        this.riderRepository = riderRepository;
        this.tripRepository = tripRepository;
    }

    //Fetches Current Session's User Email from SecurityContextHolder
    public Rider fetchCurrentRider() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("USER NOT AUTHENTICATED");
        }
        String email = auth.getName();
        return riderRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    public List<Trip> fetchRiderTrips(String userID) {
        List<String> iterableId = new ArrayList<String>();
        iterableId.add(userID);
        return tripRepository.findAllById(iterableId);
    }
}
