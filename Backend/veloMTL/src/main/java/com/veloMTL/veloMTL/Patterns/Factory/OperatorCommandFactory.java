package com.veloMTL.veloMTL.Patterns.Factory;

import com.veloMTL.veloMTL.DTO.Helper.CommandDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.Command.*;
import com.veloMTL.veloMTL.Patterns.Command.OperatorCommands.*;
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;
import org.springframework.stereotype.Service;

@Service
public class OperatorCommandFactory extends CommandFactory{
    private final DockService dockService;
    private final StationService stationService;
    private final BikeService bikeService;

    public OperatorCommandFactory(DockService dockService, StationService stationService, BikeService bikeService){
        this.dockService = dockService;
        this.stationService = stationService;
        this.bikeService = bikeService;
    }

    @Override
    public Command<?> createCommand(CommandDTO commandDTO, UserStatus role) {
        return switch (commandDTO.getCommand()) {
            //mark dock out of service
            case "MDOS" -> new MarkDockOutOfService(dockService, commandDTO.getUserId(), commandDTO.getObjectId(), role);
            //mark station out of service
            case "MSOS" -> new MarkStationOutOfService(stationService, commandDTO.getUserId(), commandDTO.getObjectId(), role);
            //restore dock
            case "RD" -> new RestoreDock(dockService, commandDTO.getUserId(), commandDTO.getObjectId(), role);
            //restore station
            case "RS" -> new RestoreStation(stationService, commandDTO.getUserId(), commandDTO.getObjectId(), role);
            //unlock bike
            case "UB" -> new OperatorUnlockBike(bikeService, commandDTO.getUserId(), commandDTO.getObjectId(), role);
            //lock bike
            case "LB" -> new OperatorLockBike(bikeService, commandDTO.getUserId(), commandDTO.getObjectId(), commandDTO.getDockId(), role);
            default -> throw new IllegalArgumentException("Unknown operator action: " + commandDTO.getCommand());
        };
    }
}
