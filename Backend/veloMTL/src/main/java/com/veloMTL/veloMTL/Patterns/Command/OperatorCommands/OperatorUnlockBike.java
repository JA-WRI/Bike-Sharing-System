package com.veloMTL.veloMTL.Patterns.Command.OperatorCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;

public class OperatorUnlockBike implements Command<ResponseDTO<BikeDTO>> {
    private final BikeService bikeService;
    private final String operatorId;
    private final String bikeId;

    public OperatorUnlockBike(BikeService bikeService, String operatorId, String bikeId) {
        this.bikeService = bikeService;
        this.operatorId = operatorId;
        this.bikeId = bikeId;
    }

    @Override
    public ResponseDTO<BikeDTO> execute() {
        return bikeService.unlockBike(bikeId, operatorId);
    }
}
