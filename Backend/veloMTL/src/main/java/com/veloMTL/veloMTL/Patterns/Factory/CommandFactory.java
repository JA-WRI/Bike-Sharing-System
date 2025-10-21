package com.veloMTL.veloMTL.Patterns.Factory;

import com.veloMTL.veloMTL.DTO.Helper.CommandDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;



public abstract class CommandFactory {
    public abstract Command<?> createCommand(CommandDTO commandDTO);
}
