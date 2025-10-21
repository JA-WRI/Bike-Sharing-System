package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

public class ReservedBikeState implements BikeState{


    @Override
    public StateChangeResponse unlockBike(Bike bike, Dock dock) { //This needs to check the user + reservation

        //**add logic here**

        

        StateChangeResponse response = new StateChangeResponse(StateChangeStatus.SUCCESS, "Bike was unlocked with reservation");
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
