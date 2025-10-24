package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

public class ReservedBikeState implements BikeState{


    @Override
    public StateChangeResponse unlockBike(Bike bike, Dock dock, UserStatus userStatus) {
        String message;

        switch(userStatus) {
            case UserStatus.OPERATOR:
                bike.setBikeStatus(BikeStatus.OUT_OF_SERVICE);
                message = "Bike is out of service and undocked";
                break;
            case UserStatus.RIDER:
                bike.setBikeStatus(BikeStatus.ON_TRIP);
                message = "Bike was unlocked with reservation";
                break;
            default:
                message = "You have to be signed in to unlock a bike";
                return new StateChangeResponse(StateChangeStatus.SUCCESS, message);
        }
        //**add logic here**

        StateChangeResponse response = new StateChangeResponse(StateChangeStatus.SUCCESS, message);
        StateChangeResponse reponse2 = new StateChangeResponse(StateChangeStatus.FAILURE, "Not your reservation");
        return response;
    }

    @Override
    public StateChangeResponse lockBike(Bike bike, Dock dock) {
        return new StateChangeResponse(StateChangeStatus.INVALID_TRANSITION, "Bike is already locked");
    }

    @Override
    public StateChangeResponse reserveBike(Bike bike) {
        return new StateChangeResponse(StateChangeStatus.NOT_ALLOWED, "Bike is already reserved");
    }

    @Override
    public StateChangeResponse markOutOfService(Bike bike) {
        //Add logic here
        return new StateChangeResponse(StateChangeStatus.SUCCESS, "Bike is put out of service and reservation is canceled");
    }
}
