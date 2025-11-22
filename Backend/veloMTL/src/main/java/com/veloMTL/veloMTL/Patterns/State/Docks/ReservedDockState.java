package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;

import java.time.LocalDateTime;

public class ReservedDockState implements DockState{

    @Override
    public StateChangeResponse reserveDock(Dock dock, String riderId, LocalDateTime reservationTime) {
        return new StateChangeResponse(StateChangeStatus.ALREADY_IN_DESIRED_STATE, "Dock already reserved");
    }

    @Override
    public StateChangeResponse markDockOutOfService(Dock dock) {
        dock.setStatus(DockStatus.OUT_OF_SERVICE);
        dock.setState(new MaintenanceDockState());
        dock.setReserveUser(null);
        return new StateChangeResponse(StateChangeStatus.SUCCESS, "Dock is set to out of service");
        //Add logic to cancel the reservation
    }

    @Override
    public StateChangeResponse restoreService(Dock dock) {
        return new StateChangeResponse(StateChangeStatus.ALREADY_IN_DESIRED_STATE, "Dock is already in service");
    }
}
