package com.veloMTL.veloMTL.Patterns.Factory;

import com.veloMTL.veloMTL.DTO.Helper.CommandDTO;
import com.veloMTL.veloMTL.Patterns.Command.*;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;
import org.springframework.stereotype.Service;

@Service
public class OperatorCommandFactory extends CommandFactory{
    private final DockService dockService;
    private final StationService stationService;

    public OperatorCommandFactory(DockService dockService, StationService stationService){
        this.dockService = dockService;
        this.stationService = stationService;
    }

    @Override
    public Command<?> createCommand(CommandDTO commandDTO) {
        return switch (commandDTO.getCommand().toLowerCase()) {
            case "mark dock out of service" -> new MarkDockOutOfService(dockService, commandDTO.getUserId(), commandDTO.getObjectId());
            case "mark station out of service" -> new MarkStationOutOfService(stationService, commandDTO.getUserId(), commandDTO.getObjectId());
            case "restore dock" -> new RestoreDock(dockService, commandDTO.getUserId(), commandDTO.getObjectId());
            case "restore station" -> new RestoreStation(stationService, commandDTO.getUserId(), commandDTO.getObjectId());
            default -> throw new IllegalArgumentException("Unknown action: " + commandDTO.getCommand());
        };
    }
}
