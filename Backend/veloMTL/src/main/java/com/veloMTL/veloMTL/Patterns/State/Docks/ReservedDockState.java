package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;

public class ReservedDockState implements DockState{

    @Override
    public String reserveDock(Dock dock) {
        return("Dock already reserved, please chose another dock");
    }


    @Override
    public String markDockOutOfService(Dock dock) {
        dock.setStatus(DockStatus.OUT_OF_SERVICE);
        dock.setState(new MaintenanceDockState());
        return ("Dock is set to out of service");
        //Add logic to cancel the reservation
    }

    @Override
    public String restoreService(Dock dock) {
        return ("Dock is already in service");
    }
}
