package com.veloMTL.veloMTL.Patterns.Factory;

import com.veloMTL.veloMTL.DTO.Helper.CommandDTO;
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
    private final StationService stationService;
    private final BikeService bikeService;
    private final TripService tripService;


    public RiderCommandFactory(DockService dockService, StationService stationService, BikeService bikeService, TripService tripService){
        this.dockService = dockService;
        this.stationService = stationService;
        this.bikeService = bikeService;
        this.tripService = tripService;
    }

    @Override
    public Command<?> createCommand(CommandDTO commandDTO) {
        return switch (commandDTO.getCommand()) {
            //unlock bike
            case "UB" -> new RiderUnlockBike(bikeService, tripService, commandDTO.getUserId(), commandDTO.getObjectId());
            //lock bike
            case "LB" -> new RiderLockBike(bikeService, tripService, commandDTO.getUserId(), commandDTO.getObjectId(), commandDTO.getDockId());
            //reserve bike
            case "RB" -> new RiderReserveBike(bikeService, commandDTO.getDockId(), commandDTO.getReserveTime(),
                    commandDTO.getObjectId(), commandDTO.getUserId());
            default -> throw new IllegalArgumentException("Unknown rider action: " + commandDTO.getCommand());
        };
    }
}
