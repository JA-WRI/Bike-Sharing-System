package com.veloMTL.veloMTL.Patterns.Command.OperatorCommands;

import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.DTO.BMSCore.StationDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;

public class MarkStationOutOfService implements Command<ResponseDTO<StationDTO>> {
    private final StationService stationService;
    private String stationId;
    private String operatorId;
    private final UserStatus role;

    public MarkStationOutOfService(StationService stationService, String operatorId, String stationId, UserStatus role) {
        this.operatorId = operatorId;
        this.stationId = stationId;
        this.stationService = stationService;
        this.role = role;
    }


    @Override
    public ResponseDTO<StationDTO> execute() {
        return stationService.markStationOutOfService(stationId, role);
    }
}
