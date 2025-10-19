package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.Dock;

public interface DockState {

    String reserveDock(Dock dock);

    //call after bike unlocked
    String emptyDock(Dock dock);

    //call after bike is returned
    String occupyDock(Dock dock);

    String markDockOutOfService(Dock dock);

    String restoreService(Dock dock);


}
