package com.veloMTL.veloMTL.Patterns.Command.RiderCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;
import com.veloMTL.veloMTL.Service.BMSCore.TripService;

public class RiderUnlockBike implements Command<ResponseDTO<BikeDTO>> {
    private final BikeService bikeService;
    private final TripService tripService;
    private final String riderId;
    private final String bikeId;

    public RiderUnlockBike(BikeService bikeService, TripService tripService, String riderId, String bikeId) {
        this.bikeService = bikeService;
        this.tripService = tripService;
        this.riderId = riderId;
        this.bikeId = bikeId;
    }

    @Override
    public ResponseDTO<BikeDTO> execute() {
        ResponseDTO<BikeDTO> responseDTO = bikeService.unlockBike(bikeId, riderId, UserStatus.RIDER);
        tripService.createTrip(new TripDTO(null, null, null, bikeId, riderId));

        return responseDTO;
    }
}
