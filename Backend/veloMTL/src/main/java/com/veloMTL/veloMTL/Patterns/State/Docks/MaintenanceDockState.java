package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;

import java.time.LocalDateTime;

public class MaintenanceDockState implements DockState{
    @Override
    public StateChangeResponse reserveDock(Dock dock, String riderId, LocalDateTime reservationTime) {
        return new StateChangeResponse(StateChangeStatus.NOT_ALLOWED, "Cannot reserve this dock");
    }

    @Override
    public StateChangeResponse markDockOutOfService(Dock dock) {
        return new StateChangeResponse(StateChangeStatus.ALREADY_IN_DESIRED_STATE, "Dock is already out of service");
    }

    @Override
    public StateChangeResponse restoreService(Dock dock) {
        dock.setStatus(DockStatus.EMPTY);
        dock.setState(new EmptyDockState());
        return new StateChangeResponse(StateChangeStatus.SUCCESS, "Dock is in service");
    }
}
