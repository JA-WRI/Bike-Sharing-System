package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.EmptyDockState;
import com.veloMTL.veloMTL.Patterns.State.Docks.OccupiedDockState;

public class AvailableBikeState implements BikeState{


    @Override
    public String unlockBike(Bike bike, Dock dock) {
        String message;
        //operator unlocking the bike
        bike.setBikeStatus(BikeStatus.OUT_OF_SERVICE);
        bike.setState(new MaintenanceBikeState());

        dock.setStatus(DockStatus.EMPTY);
        dock.setState(new EmptyDockState());

        message = "Bike is out of service and undocked";

        return message;
    }

    @Override
    public String lockBike(Bike bike, Dock dock) {
        return "Bike is already docked";
    }

    @Override
    public String reserveBike(Bike bike) {
        return "";
    }

    @Override
    public String markOutOfService(Bike bike) {
        return "Bike is already out of service";
    }
}
