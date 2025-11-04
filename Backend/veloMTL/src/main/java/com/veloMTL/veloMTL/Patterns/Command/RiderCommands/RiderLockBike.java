package com.veloMTL.veloMTL.Patterns.Command.RiderCommands;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.BMSCore.TripDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;
import com.veloMTL.veloMTL.Service.BMSCore.PaymentService;
import com.veloMTL.veloMTL.Service.BMSCore.TripService;

public class RiderLockBike implements Command<ResponseDTO<BikeDTO>> {
    private final BikeService bikeService;
    private final TripService tripService;
    private final String riderId;
    private final String bikeId;
    private final String dockId;
    private final PaymentService paymentService;

    public RiderLockBike(BikeService bikeService, TripService tripService, String riderId, String bikeId, String dockId, PaymentService paymentService) {
        this.bikeService = bikeService;
        this.tripService = tripService;
        this.riderId = riderId;
        this.bikeId = bikeId;
        this.dockId = dockId;
        this.paymentService = paymentService;
    }

    @Override
    public ResponseDTO<BikeDTO> execute() {
        ResponseDTO<BikeDTO> responseDTO = bikeService.lockBike(bikeId, riderId, dockId);
        Trip trip = tripService.findOngoingTrip(bikeId, riderId);
        if (trip != null) {
            tripService.endTrip(trip);
            //paymentService.pay(riderId, trip);
        }
        return responseDTO;
    }
}
