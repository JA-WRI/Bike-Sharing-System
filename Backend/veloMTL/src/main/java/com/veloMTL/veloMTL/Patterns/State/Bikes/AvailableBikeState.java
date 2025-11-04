package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.EmptyDockState;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;

import java.time.LocalDateTime;

public class AvailableBikeState implements BikeState{

    @Override
    public StateChangeResponse unlockBike(Bike bike, Dock dock, UserStatus role, LocalDateTime currentTime, String username) {
        String message;

        switch(role) {
            case UserStatus.OPERATOR:
                bike.setBikeStatus(BikeStatus.OUT_OF_SERVICE);
                message = "Bike is out of service and undocked";
                break;
            case UserStatus.RIDER:
                bike.setBikeStatus(BikeStatus.ON_TRIP);
                message = "Bike has been unlocked";
                break;
            default:
                message = "You have to be signed in to unlock a bike";
                return new StateChangeResponse(StateChangeStatus.FAILURE, message);
        }

        bike.setState(new MaintenanceBikeState());

        dock.setStatus(DockStatus.EMPTY);
        dock.setState(new EmptyDockState());

        StateChangeResponse response = new StateChangeResponse(StateChangeStatus.SUCCESS, message);

        return response;
    }

    @Override
    public StateChangeResponse lockBike(Bike bike, Dock dock, UserStatus role) {
        return new StateChangeResponse(StateChangeStatus.ALREADY_IN_DESIRED_STATE, "Bike is already docked");
    }

    @Override
    public StateChangeResponse reserveBike(Bike bike, Dock dock, LocalDateTime reserveTime, String reserveUser) {

        bike.setBikeStatus(BikeStatus.RESERVED);
        bike.setReserveDate(reserveTime);
        bike.setReserveUser(reserveUser);
        bike.setState(new ReservedBikeState());

        return new StateChangeResponse(StateChangeStatus.SUCCESS, "Bike has been reserved");
    }

}
