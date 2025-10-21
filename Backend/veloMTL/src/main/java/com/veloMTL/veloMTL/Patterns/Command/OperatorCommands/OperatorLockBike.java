package com.veloMTL.veloMTL.Patterns.Command.OperatorCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;

public class OperatorLockBike implements Command<ResponseDTO<BikeDTO>> {
    private final BikeService bikeService;
    private final String operatorId;
    private final String bikeId;
    private final String dockId;

    public OperatorLockBike(BikeService bikeService, String operatorId, String bikeId, String dockId) {
        this.bikeService = bikeService;
        this.operatorId = operatorId;
        this.bikeId = bikeId;
        this.dockId = dockId;
    }

    @Override
    public ResponseDTO<BikeDTO> execute() {
        return bikeService.lockBike(bikeId, operatorId, dockId);
    }
}
