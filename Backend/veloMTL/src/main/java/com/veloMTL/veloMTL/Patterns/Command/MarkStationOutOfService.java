package com.veloMTL.veloMTL.Patterns.Command;

import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.DTO.BMSCore.StationDTO;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;

public class MarkStationOutOfService implements Command<ResponseDTO<StationDTO>>{
    private final StationService stationService;
    private String stationId;
    private String operatorId;

    public MarkStationOutOfService(StationService stationService, String operatorId, String stationId) {
        this.operatorId = operatorId;
        this.stationId = stationId;
        this.stationService = stationService;
    }


    @Override
    public ResponseDTO<StationDTO> execute() {
        return stationService.markStationOutOfService(stationId);
    }
}
