package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;

public class ReservedBikeState implements BikeState{


    @Override
    public String unlockBike(Bike bike, Dock dock) {
        return "";
    }

    @Override
    public String lockBike(Bike bike, Dock dock) {
        return "";
    }

    @Override
    public String reserveBike(Bike bike) {
        return "";
    }

    @Override
    public String markOutOfService(Bike bike) {
        return "";
    }
}
