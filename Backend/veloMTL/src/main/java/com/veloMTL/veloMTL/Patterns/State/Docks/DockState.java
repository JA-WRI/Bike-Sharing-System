package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;

public interface DockState {

    String reserveDock(Dock dock);


    String markDockOutOfService(Dock dock);

    String restoreService(Dock dock);


}
