package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.EmptyDockState;
import com.veloMTL.veloMTL.Patterns.State.Docks.OccupiedDockState;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

import java.time.LocalDateTime;

public class ReservedBikeState implements BikeState{


    @Override
    public StateChangeResponse unlockBike(Bike bike, Dock dock) { //This needs to check the user + reservation

        //**add logic here**


        StateChangeResponse response = new StateChangeResponse(StateChangeStatus.SUCCESS, "Bike was unlocked with reservation");
        StateChangeResponse reponse2 = new StateChangeResponse(StateChangeStatus.FAILURE, "Not your reservation");
        return response;
    }

    public StateChangeResponse unlockBike(Bike bike, Dock dock, LocalDateTime currentTime, String reserveUser) {
        StateChangeResponse response;
        if (bike.getReserveDate().isBefore(currentTime)) { //If reserveUser does not unlock before the reservation time
            bike.setBikeStatus(BikeStatus.AVAILABLE);
            dock.setStatus(DockStatus.OCCUPIED);
            bike.setState(new AvailableBikeState());
            dock.setState(new OccupiedDockState());
            response = new StateChangeResponse(StateChangeStatus.FAILURE, "Reservation has expired :(");
        } else if (!bike.getReserveUser().equals(reserveUser)) { // If user attempting to unlock is not correct
            response = new StateChangeResponse(StateChangeStatus.FAILURE, "Not your reservation");
        } else {
            bike.setBikeStatus(BikeStatus.ON_TRIP);
            dock.setStatus(DockStatus.EMPTY);
            bike.setState(new OnTripBikeState());
            dock.setState(new EmptyDockState());
            response = new StateChangeResponse(StateChangeStatus.SUCCESS, "Bike was unlocked with reservation");
        }
        return response;
    }

    @Override
    public StateChangeResponse lockBike(Bike bike, Dock dock) {
        return new StateChangeResponse(StateChangeStatus.INVALID_TRANSITION, "Bike is already locked");
    }

    @Override
    public StateChangeResponse reserveBike(Bike bike, Dock dock, LocalDateTime reserveTime, String reserveUser) {
        return new StateChangeResponse(StateChangeStatus.NOT_ALLOWED, "Bike is already reserved");
    }

    @Override
    public StateChangeResponse markOutOfService(Bike bike) {
        //Add logic here
        return new StateChangeResponse(StateChangeStatus.SUCCESS, "Bike is put out of service and reservation is canceled");
    }
}
