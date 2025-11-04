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

public class OnTripBikeState implements BikeState{


    @Override
    public StateChangeResponse unlockBike(Bike bike, Dock dock, UserStatus userStatus, LocalDateTime currentTime, String username) {
        return new StateChangeResponse(StateChangeStatus.NOT_ALLOWED, "Bike is currently being used");
    }

    @Override
    public StateChangeResponse lockBike(Bike bike, Dock dock) {

        if (dock.getStatus() == DockStatus.EMPTY) {
            // Dock is empty — put bike back
            bike.setState(new AvailableBikeState());
            bike.setReserveUser(null);
            bike.setReserveDate(null);
            bike.setDock(dock);
            bike.setBikeStatus(BikeStatus.AVAILABLE);

            dock.setState(new OccupiedDockState());
            dock.setStatus(DockStatus.OCCUPIED);
            dock.setBike(bike.getBikeId());
            dock.setReserveDate(null);
            dock.setReserveUser(null);

            return new StateChangeResponse(StateChangeStatus.SUCCESS, "Bike has been successfully put back");

        } else if (dock.getStatus() == DockStatus.RESERVED) {

            LocalDateTime reserveDate = dock.getReserveDate();

            // Make sure reserveDate is not null (avoid NullPointerException)
            if (reserveDate != null && LocalDateTime.now().isAfter(reserveDate.plusMinutes(15))) {
                // Reservation expired
                dock.setStatus(DockStatus.EMPTY);
                dock.setState(new EmptyDockState());
                dock.setReserveDate(null);
                dock.setReserveUser(null);

                return new StateChangeResponse(StateChangeStatus.FAILURE, "Reservation has expired");
            } else {
                // Reservation still valid — allow putting the bike back
                bike.setBikeStatus(BikeStatus.AVAILABLE);
                bike.setState(new AvailableBikeState());
                bike.setReserveUser(null);
                bike.setReserveDate(null);

                dock.setState(new EmptyDockState());
                dock.setStatus(DockStatus.EMPTY);
                dock.setBike(bike.getBikeType());
                dock.setReserveDate(null);
                dock.setReserveUser(null);

                return new StateChangeResponse(StateChangeStatus.SUCCESS, "Bike has been successfully put back");
            }

        } else {
            return new StateChangeResponse(StateChangeStatus.NOT_ALLOWED, "Dock is not available");
        }
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
