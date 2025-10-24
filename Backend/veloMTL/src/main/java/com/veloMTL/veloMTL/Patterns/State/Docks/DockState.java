package com.veloMTL.veloMTL.Patterns.State.Docks;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

import java.time.LocalDateTime;

public interface DockState {

    StateChangeResponse reserveDock(Dock dock, String riderId, LocalDateTime reservationTime);


    StateChangeResponse markDockOutOfService(Dock dock);

    StateChangeResponse restoreService(Dock dock);


}
