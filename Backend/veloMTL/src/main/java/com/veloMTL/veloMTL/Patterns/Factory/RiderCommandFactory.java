package com.veloMTL.veloMTL.Patterns.Factory;

import com.veloMTL.veloMTL.DTO.Helper.CommandDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Patterns.Command.OperatorCommands.*;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;
import org.springframework.stereotype.Service;

@Service
public class RiderCommandFactory extends CommandFactory{
    private final DockService dockService;


    public RiderCommandFactory(DockService dockService) {
        this.dockService = dockService;
    }


    @Override
    public Command<?> createCommand(CommandDTO commandDTO) {
        return null;
//        return switch (commandDTO.getCommand()) {
//            //mark dock out of service
//            case "MDOS" -> new MarkDockOutOfService(dockService, commandDTO.getUserId(), commandDTO.getObjectId());
//            //mark station out of service
//            case "MSOS" -> new MarkStationOutOfService(stationService, commandDTO.getUserId(), commandDTO.getObjectId());
//            //restore dock
//            case "RD" -> new RestoreDock(dockService, commandDTO.getUserId(), commandDTO.getObjectId());
//            //restore station
//            case "RS" -> new RestoreStation(stationService, commandDTO.getUserId(), commandDTO.getObjectId());
//            //unlock bike
//            case "UB" -> new OperatorUnlockBike(bikeService, commandDTO.getUserId(), commandDTO.getObjectId());
//            //lock bike
//            case "LB" -> new OperatorLockBike(bikeService, commandDTO.getUserId(), commandDTO.getObjectId(), commandDTO.getDockId());
//            default -> throw new IllegalArgumentException("Unknown action: " + commandDTO.getCommand());
//        };
    }
}
