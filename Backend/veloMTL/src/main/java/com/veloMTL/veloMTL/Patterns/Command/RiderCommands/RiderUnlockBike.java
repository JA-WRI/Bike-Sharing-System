package com.veloMTL.veloMTL.Patterns.Command.RiderCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;


public class RiderUnlockBike implements Command<ResponseDTO<BikeDTO>> {
    private final BikeService bikeService;
    private final String riderId;
    private final String bikeId;
    private final UserStatus role;

    public RiderUnlockBike(BikeService bikeService, String riderId, String bikeId, UserStatus role) {
        this.bikeService = bikeService;
        this.riderId = riderId;
        this.bikeId = bikeId;
        this.role = role;
    }

    @Override
    public ResponseDTO<BikeDTO> execute() {
        ResponseDTO<BikeDTO> responseDTO = bikeService.unlockBike(bikeId, riderId, role);
        return responseDTO;
    }
}
