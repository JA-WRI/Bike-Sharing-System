package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

import java.time.LocalDateTime;

public interface BikeState {

    StateChangeResponse unlockBike(Bike bike, Dock dock);

    StateChangeResponse lockBike(Bike bike, Dock dock);

    StateChangeResponse reserveBike(Bike bike, Dock dock, LocalDateTime reserveTime, String reserveUser);

    StateChangeResponse markOutOfService(Bike bike);
}
