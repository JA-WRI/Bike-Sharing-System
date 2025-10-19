package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;

public class OccupiedDockState implements DockState{
    @Override
    public String reserveDock(Dock dock) {
        return ("Dock is currently occupied select another dock to reserve");

    }
    @Override
    public String emptyDock(Dock dock) {
        dock.setStatus(DockStatus.EMPTY);
        dock.setState(new EmptyDockState());
        return ("Bike was successfully undocked");
    }

    @Override
    public String occupyDock(Dock dock) {
        return ("Dock is occupied, please return bike to empty dock");
    }

    @Override
    public String markDockOutOfService(Dock dock) {
        return ("Please remove bike from dock before setting it to out of service");

    }

    @Override
    public String restoreService(Dock dock) {
        return ("Dock is already in service");
    }
}
