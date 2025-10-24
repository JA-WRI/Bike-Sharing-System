package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

import java.time.LocalDateTime;

public class OccupiedDockState implements DockState{

    @Override
    public StateChangeResponse reserveDock(Dock dock, String riderId, LocalDateTime reservationTime) {
        return new StateChangeResponse(StateChangeStatus.NOT_ALLOWED, "Dock is currently occupied");

    }

    @Override
    public StateChangeResponse markDockOutOfService(Dock dock) {
        return new StateChangeResponse(StateChangeStatus.INVALID_TRANSITION, "Bike needs to be removed first");

    }

    @Override
    public StateChangeResponse restoreService(Dock dock) {
        return new StateChangeResponse(StateChangeStatus.ALREADY_IN_DESIRED_STATE, "Dock is already in service");
    }
}
