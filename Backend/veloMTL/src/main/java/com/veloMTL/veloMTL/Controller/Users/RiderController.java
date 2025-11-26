package com.veloMTL.veloMTL.Controller.Users;

import com.veloMTL.veloMTL.DTO.Helper.CommandDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Patterns.Factory.RiderCommandFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/riders")
public class RiderController {

    private final RiderCommandFactory commandFactory;
    
    public RiderController(RiderCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    @PostMapping("/command")
    public ResponseEntity<ResponseDTO<?>> executeCommand(@RequestBody CommandDTO commandDTO){
        // Process command with RIDER status - allows operators to use rider endpoints
        // and have commands process as if they are riders
        Command<?> action = commandFactory.createCommand(commandDTO,UserStatus.RIDER );
        ResponseDTO<?> response = (ResponseDTO<?>) action.execute();
        return ResponseEntity.ok(response);
    }

}

