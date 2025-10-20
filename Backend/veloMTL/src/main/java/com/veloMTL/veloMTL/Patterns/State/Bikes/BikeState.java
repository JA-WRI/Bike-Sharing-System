package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;

public interface BikeState {

    String unlockBike(Bike bike, Dock dock);

    String lockBike(Bike bike, Dock dock);

    String reserveBike(Bike bike);

    String markOutOfService(Bike bike);
}
