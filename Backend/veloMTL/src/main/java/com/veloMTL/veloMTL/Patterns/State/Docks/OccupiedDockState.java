package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;

public class OccupiedDockState implements DockState{
    @Override
    public String reserveDock(Dock dock) {
        return ("Dock is currently occupied select another dock to reserve");

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
