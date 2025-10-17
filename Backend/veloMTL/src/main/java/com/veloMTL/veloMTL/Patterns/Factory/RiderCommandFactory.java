package com.veloMTL.veloMTL.Patterns.Factory;

import com.veloMTL.veloMTL.DTO.CommandDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;
import org.springframework.stereotype.Service;

@Service
public class RiderCommandFactory extends CommandFactory{
    private final DockService dockService;


    public RiderCommandFactory(DockService dockService) {
        this.dockService = dockService;
    }


    @Override
    public Command createCommand(CommandDTO commandDTO) {
        return null;
    }
}
