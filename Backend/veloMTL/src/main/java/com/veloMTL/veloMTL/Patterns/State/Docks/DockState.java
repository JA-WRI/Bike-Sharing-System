package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

public interface DockState {

    StateChangeResponse reserveDock(Dock dock);


    StateChangeResponse markDockOutOfService(Dock dock);

    StateChangeResponse restoreService(Dock dock);


}
