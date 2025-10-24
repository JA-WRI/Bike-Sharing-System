package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

public interface BikeState {

    StateChangeResponse unlockBike(Bike bike, Dock dock, UserStatus userStatus);

    StateChangeResponse lockBike(Bike bike, Dock dock);

    StateChangeResponse reserveBike(Bike bike);

    StateChangeResponse markOutOfService(Bike bike);
}
