package com.veloMTL.veloMTL.Patterns.Factory;

import com.veloMTL.veloMTL.DTO.CommandDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Patterns.Command.DockMaintenanceCommand;
import com.veloMTL.veloMTL.Patterns.Command.StationEmptyCommand;
import com.veloMTL.veloMTL.Patterns.Command.StationMaintenanceCommand;
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
    public Command createCommand(CommandDTO commandDTO) {
        return switch (commandDTO.getCommand().toLowerCase()) {
            case "dockmaintenance" -> new DockMaintenanceCommand(dockService, commandDTO.getUserId(), commandDTO.getObjectId());
            case "stationmaintenance" -> new StationMaintenanceCommand(stationService, commandDTO.getUserId(), commandDTO.getObjectId());
            case "stationempty" -> new StationEmptyCommand(stationService, commandDTO.getUserId(), commandDTO.getObjectId());
            default -> throw new IllegalArgumentException("Unknown action: " + commandDTO.getCommand());
        };
    }
}
