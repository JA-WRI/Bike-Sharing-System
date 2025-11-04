package com.veloMTL.veloMTL.Patterns.Factory;

import com.veloMTL.veloMTL.DTO.Helper.CommandDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Patterns.Command.RiderCommands.*;
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;
import com.veloMTL.veloMTL.Service.BMSCore.TripService;
import com.veloMTL.veloMTL.Service.Users.RiderService;
import org.springframework.stereotype.Service;

@Service
public class RiderCommandFactory extends CommandFactory{
    private final DockService dockService;
    private final BikeService bikeService;


    public RiderCommandFactory(DockService dockService, BikeService bikeService, TripService tripService){
        this.dockService = dockService;
        this.bikeService = bikeService;
    }

    @Override
    public Command<?> createCommand(CommandDTO commandDTO, UserStatus role) {
        return switch (commandDTO.getCommand()) {
            //unlock bike
            case "UB" -> new RiderUnlockBike(bikeService, commandDTO.getUserId(), commandDTO.getObjectId(), role);
            //lock bike
            case "LB" -> new RiderLockBike(bikeService, commandDTO.getUserId(), commandDTO.getObjectId(), commandDTO.getDockId(), role);
            //reserve bike
            case "RB" -> new RiderReserveBike(bikeService, commandDTO.getDockId(), commandDTO.getReserveTime(),
                    commandDTO.getObjectId(), commandDTO.getUserId(), role);
            //reserving a dock
            case "RD" -> new ReserveDock(dockService, commandDTO.getUserId(), commandDTO.getDockId(), commandDTO.getReserveTime(), role);
            default -> throw new IllegalArgumentException("Unknown rider action: " + commandDTO.getCommand());
        };
    }
}
