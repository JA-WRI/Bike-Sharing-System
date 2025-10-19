package com.veloMTL.veloMTL.Patterns.Command;

import com.veloMTL.veloMTL.DTO.ResponseDTO;
import com.veloMTL.veloMTL.DTO.StationDTO;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;

public class MarkStationOutOfService implements Command<ResponseDTO<StationDTO>>{
    private final StationService stationService;
    private String stationId;
    private String operatorId;

    public MarkStationOutOfService(StationService stationService, String stationId, String operatorId){
        this.operatorId = operatorId;
        this.stationId = stationId;
        this.stationService = stationService;
    }


    @Override
    public ResponseDTO<StationDTO> execute() {
        return stationService.markStationOutOfService(stationId);
    }
}
