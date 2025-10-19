package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;

public class MaintenanceDockState implements DockState{
    @Override
    public String reserveDock(Dock dock) {
        return "Cannot reserve this dock";
    }

    @Override
    public String emptyDock(Dock dock) {
        return "Cannot take out bike from this dock";

    }

    @Override
    public String occupyDock(Dock dock) {
        return "Cannot return bike to this dock";
    }

    @Override
    public String markDockOutOfService(Dock dock) {
        return "Dock is already out of service";
    }

    @Override
    public String restoreService(Dock dock) {
        dock.setStatus(DockStatus.EMPTY);
        dock.setState(new EmptyDockState());
        return ("Dock is in service");
    }
}
