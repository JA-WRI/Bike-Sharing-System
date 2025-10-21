package com.veloMTL.veloMTL.Patterns.State.Stations;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Patterns.State.Docks.MaintenanceDockState;
import com.veloMTL.veloMTL.Service.NotificationService;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

import java.util.List;

public class EmptyStationState implements StationState, OccupancyChange{

    private NotificationService notificationService;

    public EmptyStationState(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public StateChangeResponse markStationOutOfService(Station station) {
        List<Dock> docks = station.getDocks();
        for(Dock dock: docks){
            dock.setStatus(DockStatus.OUT_OF_SERVICE);
            dock.setState(new MaintenanceDockState());
        }
        station.setStationStatus(StationStatus.OUT_OF_SERVICE);
        station.setStationState(new MaintenanceStationState(notificationService));
        return new StateChangeResponse(StateChangeStatus.SUCCESS,"Station was marked out of service");


    }

    @Override
    public StateChangeResponse restoreStation(Station station) {
        return new StateChangeResponse(StateChangeStatus.ALREADY_IN_DESIRED_STATE, "Station is already in service");
    }

    @Override
    public void handleOccupancyChange(Station station) {
        String message = "Station '" + station.getStationName() + "' is now EMPTY!";
        notificationService.notifyOperators(message);
    }
}
