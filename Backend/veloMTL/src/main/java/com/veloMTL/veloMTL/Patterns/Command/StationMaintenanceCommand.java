package com.veloMTL.veloMTL.Patterns.Command;

import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;

public class StationMaintenanceCommand implements Command{
    private final StationService stationService;
    private String stationId;
    private String operatorId;

    public StationMaintenanceCommand(StationService stationService, String stationId, String operatorId){
        this.operatorId = operatorId;
        this.stationId = stationId;
        this.stationService = stationService;
    }


    @Override
    public void execute() {
        stationService.updateStationStatus(stationId, StationStatus.OUT_OF_SERVICE);
    }
}
