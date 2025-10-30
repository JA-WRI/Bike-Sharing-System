package com.veloMTL.veloMTL.Patterns.Factory;

import com.veloMTL.veloMTL.DTO.Helper.CommandDTO;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Patterns.Command.RiderCommands.*;
import com.veloMTL.veloMTL.Service.BMSCore.*;
import com.veloMTL.veloMTL.Service.Users.RiderService;
import org.springframework.stereotype.Service;

@Service
public class RiderCommandFactory extends CommandFactory{
    private final DockService dockService;
    private final BikeService bikeService;
    private final TripService tripService;
    private final PaymentService paymentService;

    public RiderCommandFactory(DockService dockService, BikeService bikeService, TripService tripService, PaymentService paymentService){
        this.dockService = dockService;
        this.bikeService = bikeService;
        this.tripService = tripService;
        this.paymentService = paymentService;
    }

    @Override
    public Command<?> createCommand(CommandDTO commandDTO) {
        return switch (commandDTO.getCommand()) {
            //unlock bike
            case "UB" -> new RiderUnlockBike(bikeService, tripService, commandDTO.getUserId(), commandDTO.getObjectId());
            //lock bike
            case "LB" -> new RiderLockBike(bikeService, tripService, commandDTO.getUserId(), commandDTO.getObjectId(), commandDTO.getDockId(), paymentService);
            //reserve bike
            case "RB" -> new RiderReserveBike(bikeService, commandDTO.getDockId(), commandDTO.getReserveTime(),
                    commandDTO.getObjectId(), commandDTO.getUserId());
            //reserving a dock
            case "RD" -> new ReserveDock(dockService, commandDTO.getUserId(), commandDTO.getDockId(), commandDTO.getReserveTime());
            default -> throw new IllegalArgumentException("Unknown rider action: " + commandDTO.getCommand());
        };
    }
}
