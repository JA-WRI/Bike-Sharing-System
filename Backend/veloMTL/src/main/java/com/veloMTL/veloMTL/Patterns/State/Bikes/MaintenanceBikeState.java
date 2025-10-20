package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.EmptyDockState;
import com.veloMTL.veloMTL.Patterns.State.Docks.OccupiedDockState;

public class MaintenanceBikeState implements BikeState {

    @Override
    public String unlockBike(Bike bike, Dock dock) {
        return "";
    }

    @Override
    public String lockBike(Bike bike, Dock dock) {
        bike.setBikeStatus(BikeStatus.AVAILABLE);
        bike.setState(new AvailableBikeState());
        bike.setDock(dock);

        dock.setBike(bike);
        dock.setStatus(DockStatus.OCCUPIED);
        dock.setState(new OccupiedDockState());

        return "Bike is back in service and docked";
    }

    @Override
    public String reserveBike(Bike bike) {
        return "";
    }

    @Override
    public String markOutOfService(Bike bike) {
        return "";
    }
}
