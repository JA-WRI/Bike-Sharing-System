package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.EmptyDockState;
import com.veloMTL.veloMTL.Patterns.State.Docks.OccupiedDockState;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;

import java.time.LocalDateTime;

public class ReservedBikeState implements BikeState{


    @Override
    public StateChangeResponse unlockBike(Bike bike, Dock dock, UserStatus role, LocalDateTime currentTime, String username) {
        StateChangeResponse response;

        //needs to cancel the reservation figure out how to do that later
        switch(role) {
            case UserStatus.OPERATOR:
                bike.setBikeStatus(BikeStatus.OUT_OF_SERVICE);
                dock.setStatus(DockStatus.EMPTY);
                dock.setState(new EmptyDockState());
                bike.setState(new MaintenanceBikeState());
                response = new StateChangeResponse(StateChangeStatus.SUCCESS, "Bike is out of service and undocked");
                break;
            case UserStatus.RIDER:
                response = unlockPrivateBike(bike, dock, currentTime, username);
                break;
            default:
                return new StateChangeResponse(StateChangeStatus.SUCCESS, "You have to be signed in to unlock a bike");
        }
        return response;
    }

    @Override
    public StateChangeResponse lockBike(Bike bike, Dock dock, UserStatus userStatus) {
        return new StateChangeResponse(StateChangeStatus.INVALID_TRANSITION, "Bike is already locked");
    }

    @Override
    public StateChangeResponse reserveBike(Bike bike, Dock dock, LocalDateTime reserveTime, String reserveUser) {
        return new StateChangeResponse(StateChangeStatus.NOT_ALLOWED, "Bike is already reserved");
    }

    private StateChangeResponse unlockPrivateBike(Bike bike, Dock dock, LocalDateTime currentTime, String reserveUser) {
        StateChangeResponse response;
        if (currentTime.isAfter(bike.getReserveDate().plusMinutes(15))) { //If reserveUser does not unlock before the reservation time
            bike.setBikeStatus(BikeStatus.AVAILABLE);
            dock.setStatus(DockStatus.OCCUPIED);
            bike.setState(new AvailableBikeState());
            dock.setState(new OccupiedDockState());
            bike.setReserveUser(null);
            bike.setReserveDate(null);

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
}
