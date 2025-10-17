package com.veloMTL.veloMTL.Controller;

import com.veloMTL.veloMTL.DTO.CommandDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Patterns.Factory.RiderCommandFactory;
import com.veloMTL.veloMTL.Service.Users.RiderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/riders")
public class RiderController {

    private final RiderService riderService; // change to facade obj
    private final RiderCommandFactory commandFactory;

    public RiderController(RiderService riderService, RiderCommandFactory commandFactory) {
        this.riderService = riderService;
        this.commandFactory = commandFactory;
    }

    @PostMapping("/action")
    public ResponseEntity<String> executeCommand(@RequestBody CommandDTO commandDTO){
        try {
            Command command = commandFactory.createCommand(commandDTO);
            command.execute();
            return ResponseEntity.ok("Command executed successfully: " + commandDTO.getCommand());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid command: " + commandDTO.getCommand());
    }

    }
}
