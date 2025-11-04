package com.veloMTL.veloMTL.Patterns.Command.OperatorCommands;

import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.DTO.BMSCore.StationDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;

public class RestoreStation implements Command<ResponseDTO<StationDTO>> {
    private final StationService stationService;
    private final String stationId;
    private final String operatorId;
    private final UserStatus role;

    public RestoreStation(StationService stationService, String operatorId, String stationId, UserStatus role) {
        this.operatorId = operatorId;
        this.stationId = stationId;
        this.stationService = stationService;
        this.role = role;
    }


    @Override
    public ResponseDTO<StationDTO> execute() {
        return stationService.restoreStation(stationId, role);
    }
}
