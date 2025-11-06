package com.veloMTL.veloMTL.PCR.Strategy;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;

public interface Plan {
    double calculateTripCost(long tripDuration, boolean isEbike);

    int getBaseFee();
}  
