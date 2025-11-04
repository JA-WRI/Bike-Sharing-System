package com.veloMTL.veloMTL.Patterns.Command.RiderCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;

public class RiderLockBike implements Command<ResponseDTO<BikeDTO>> {
    private final BikeService bikeService;
    private final String userId;
    private final String bikeId;
    private final String dockId;
    private final UserStatus role;

    public RiderLockBike(BikeService bikeService, String userId, String bikeId, String dockId, UserStatus role) {
        this.bikeService = bikeService;
        this.userId = userId;
        this.bikeId = bikeId;
        this.dockId = dockId;
        this.role = role;
    }

    @Override
    public ResponseDTO<BikeDTO> execute() {
        return bikeService.lockBike(bikeId, userId, dockId, role);
    }
}
