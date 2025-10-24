package com.veloMTL.veloMTL.Patterns.State.Stations;

import com.veloMTL.veloMTL.Model.BMSCore.Station;
import com.veloMTL.veloMTL.Model.Enums.StateChangeStatus;
import com.veloMTL.veloMTL.Service.BMSCore.NotificationService;
import com.veloMTL.veloMTL.untils.Responses.StateChangeResponse;

public class FullStationState implements StationState, OccupancyChange{
    private final NotificationService notificationService;

    public FullStationState(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public StateChangeResponse markStationOutOfService(Station station) {
        return new StateChangeResponse(StateChangeStatus.INVALID_TRANSITION, "Please empty station before marking it as out og service.");
    }

    @Override
    public StateChangeResponse restoreStation(Station station) {
        return new StateChangeResponse(StateChangeStatus.ALREADY_IN_DESIRED_STATE, "Station is already in service");
    }

    @Override
    public void handleOccupancyChange(Station station) {
        String message = "Station '" + station.getStationName() + "' is now FULL!";
        notificationService.notifyOperators(message);

    }
}
