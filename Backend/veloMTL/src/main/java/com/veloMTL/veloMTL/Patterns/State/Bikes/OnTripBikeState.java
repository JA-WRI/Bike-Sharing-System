package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

import java.time.LocalDateTime;

public class OnTripBikeState implements BikeState{


    @Override
    public StateChangeResponse unlockBike(Bike bike, Dock dock, UserStatus userStatus, LocalDateTime currentTime, String username) {
        return new StateChangeResponse(StateChangeStatus.NOT_ALLOWED, "Bike is currently being used");
    }

    @Override
    public StateChangeResponse lockBike(Bike bike, Dock dock) {

        bike.setBikeStatus(BikeStatus.AVAILABLE);
        bike.setState(new AvailableBikeState());
        return new StateChangeResponse(StateChangeStatus.SUCCESS, "Bike has been successfully put back");
    }

    @Override
    public StateChangeResponse reserveBike(Bike bike, Dock dock, LocalDateTime reserveTime, String reserveUser) {
        return new StateChangeResponse(StateChangeStatus.NOT_ALLOWED, "Bike is currently being used");
    }

    @Override
    public StateChangeResponse markOutOfService(Bike bike) {
        return new StateChangeResponse(StateChangeStatus.NOT_ALLOWED, "Bike is currently being used");
    }
}
