package com.veloMTL.veloMTL.PCR.Strategy;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;

public interface Plan {

    double calculateTripCost(long tripDuration, boolean isEbike, double flexDollars, RiderRepository riderRepository, String riderId, int arrivalStationOccupancy);
    int getBaseFee();
    double getRatebyMinute();
    void addFlexDollars(Rider rider, int arrivalStationOccupancy);
}  
