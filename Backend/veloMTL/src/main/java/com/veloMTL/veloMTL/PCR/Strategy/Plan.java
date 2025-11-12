package com.veloMTL.veloMTL.PCR.Strategy;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;

public interface Plan {

    double calculateTripCostRider(long tripDuration, boolean isEbike, double flexDollars, RiderRepository riderRepository, String riderId, int arrivalStationOccupancy);
    double calculateTripCostOperator(long tripDuration, boolean isEbike, double flexDollars, OperatorRepository operatorRepository, String operatorId, int arrivalStationOccupancy);
    int getBaseFee();
    double getRatebyMinute();
    void addFlexDollarsRider (Rider rider, int arrivalStationOccupancy);
}  
