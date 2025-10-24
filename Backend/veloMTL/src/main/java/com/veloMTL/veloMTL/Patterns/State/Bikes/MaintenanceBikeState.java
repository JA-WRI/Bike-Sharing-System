package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.EmptyDockState;
import com.veloMTL.veloMTL.Patterns.State.Docks.OccupiedDockState;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

import java.time.LocalDateTime;

public class MaintenanceBikeState implements BikeState {

    @Override
    public StateChangeResponse unlockBike(Bike bike, Dock dock, UserStatus userStatus) {

        return new StateChangeResponse(StateChangeStatus.INVALID_TRANSITION, "Bike is already undocked and out of service");
    }

    @Override
    public StateChangeResponse lockBike(Bike bike, Dock dock) {
        //only operators can do this
        bike.setBikeStatus(BikeStatus.AVAILABLE);
        bike.setState(new AvailableBikeState());
        bike.setDock(dock);

        dock.setBike(bike);
        dock.setStatus(DockStatus.OCCUPIED);
        dock.setState(new OccupiedDockState());

        return new StateChangeResponse(StateChangeStatus.SUCCESS, "Bike is back in service and docked");
    }

    @Override
    public StateChangeResponse reserveBike(Bike bike, Dock dock, LocalDateTime reserveDate, String reserveUser) {
        return new StateChangeResponse(StateChangeStatus.INVALID_TRANSITION, "Cannot reserve a bike that is out of service");
    }

    @Override
    public StateChangeResponse markOutOfService(Bike bike) {
        return new StateChangeResponse(StateChangeStatus.ALREADY_IN_DESIRED_STATE, "Bike is already out of service");
    }
}
