package com.veloMTL.veloMTL.Patterns.Command.RiderCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;

public class RiderReserveBike implements Command<ResponseDTO<BikeDTO>> {
    private final BikeService bikeService;
    private final String riderId;
    private final String bikeId;

    public RiderReserveBike(BikeService bikeService, String riderId, String bikeId) {
        this.bikeService = bikeService;
        this.riderId = riderId;
        this.bikeId = bikeId;
    }

    @Override
    public ResponseDTO<BikeDTO> execute() {
        // Not implemented yet
        // return bikeService.reserveBike(bikeId, riderId);
        return null;
    }
}
