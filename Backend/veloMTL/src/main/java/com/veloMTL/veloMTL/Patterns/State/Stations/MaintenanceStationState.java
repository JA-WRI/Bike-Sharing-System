package com.veloMTL.veloMTL.Patterns.State.Stations;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Patterns.State.Docks.EmptyDockState;
import com.veloMTL.veloMTL.Service.BMSCore.NotificationService;
import com.veloMTL.veloMTL.utils.Responses.StateChangeResponse;

import java.util.List;

public class MaintenanceStationState implements StationState{

    private final NotificationService notificationService;

    public MaintenanceStationState(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public StateChangeResponse markStationOutOfService(Station station) {
        return new StateChangeResponse(StateChangeStatus.ALREADY_IN_DESIRED_STATE, "Station is already out of service");
    }

    @Override
    public StateChangeResponse restoreStation(Station station) {
        List<Dock> docks = station.getDocks();
        for (Dock dock : docks) {
            dock.setStatus(DockStatus.EMPTY);
            dock.setState(new EmptyDockState());

        }
        station.setStationStatus(StationStatus.EMPTY);
        station.setStationState(new EmptyStationState(notificationService));
        return new StateChangeResponse(StateChangeStatus.SUCCESS, "Station status is restored");
    }
}
