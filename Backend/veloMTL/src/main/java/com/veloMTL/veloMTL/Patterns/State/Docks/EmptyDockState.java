package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;

public class EmptyDockState implements DockState{


    @Override
    public String reserveDock(Dock dock) {
        dock.setStatus(DockStatus.RESERVED);
        dock.setState(new ReservedDockState());
        return "Dock has been reserved";
    }

    //to take out a bike and set the dock to empty
    @Override
    public String emptyDock(Dock dock) {
        return ("Dock is already empty.");
    }

    //returning a bike to an empty dock
    @Override
    public String occupyDock(Dock dock) {
        dock.setStatus(DockStatus.OCCUPIED);
        dock.setState(new OccupiedDockState());
        return ("Bike was successfully docked");
    }

    @Override
    public String markDockOutOfService(Dock dock) {
        dock.setStatus(DockStatus.OUT_OF_SERVICE);
        dock.setState(new MaintenanceDockState());
        return ("Dock is set to out of service");

    }

    @Override
    public String restoreService(Dock dock) {
        return ("Dock is already in service");
    }
}
