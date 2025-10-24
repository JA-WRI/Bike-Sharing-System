package com.veloMTL.veloMTL.Patterns.State.Bikes;

import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.BikeStatus;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.State.Docks.OccupiedDockState;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

public class OnTripBikeState implements BikeState{


    @Override
    public StateChangeResponse unlockBike(Bike bike, Dock dock, UserStatus userStatus) {
        return new StateChangeResponse(StateChangeStatus.NOT_ALLOWED, "Bike is currently being used");
    }

    @Override
    public StateChangeResponse lockBike(Bike bike, Dock dock) {

        //***add logic here***
        bike.setBikeStatus(BikeStatus.AVAILABLE);
        bike.setState(new AvailableBikeState());
        bike.setDock(dock);

        dock.setBike(bike);
        dock.setStatus(DockStatus.OCCUPIED);
        dock.setState(new OccupiedDockState());

        return new StateChangeResponse(StateChangeStatus.SUCCESS, "Bike has been successfully put back");
    }

    @Override
    public StateChangeResponse reserveBike(Bike bike) {
        return new StateChangeResponse(StateChangeStatus.NOT_ALLOWED, "Bike is currently being used");
    }

    @Override
    public StateChangeResponse markOutOfService(Bike bike) {
        return new StateChangeResponse(StateChangeStatus.NOT_ALLOWED, "Bike is currently being used");
    }
}
