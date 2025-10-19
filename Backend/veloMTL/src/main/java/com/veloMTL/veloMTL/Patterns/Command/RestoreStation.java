package com.veloMTL.veloMTL.Patterns.Command;

import com.veloMTL.veloMTL.DTO.ResponseDTO;
import com.veloMTL.veloMTL.DTO.StationDTO;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;

public class RestoreStation implements Command<ResponseDTO<StationDTO>>{
    private final StationService stationService;
    private final String stationId;
    private final String operatorId;

    public RestoreStation(StationService stationService, String operatorId, String stationId) {
        this.operatorId = operatorId;
        this.stationId = stationId;
        this.stationService = stationService;
    }


    @Override
    public ResponseDTO<StationDTO> execute() {
        return stationService.restoreStation(stationId);
    }
}
