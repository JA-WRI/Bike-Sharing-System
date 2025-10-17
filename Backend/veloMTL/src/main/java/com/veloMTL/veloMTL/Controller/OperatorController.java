package com.veloMTL.veloMTL.Controller;

import com.veloMTL.veloMTL.DTO.CommandDTO;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.DTO.Users.OperatorDTO;
import com.veloMTL.veloMTL.Patterns.Factory.OperatorCommandFactory;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;
import com.veloMTL.veloMTL.Service.Users.OperatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/operators")
public class OperatorController {
    private final OperatorService operatorService;
    private final DockService dockService;
    private final StationService stationService;
    private final OperatorCommandFactory commandFactory;

    public OperatorController(OperatorService operatorService, DockService dockService, StationService stationService, OperatorCommandFactory commandFactory) {
        this.operatorService = operatorService;
        this.dockService = dockService;
        this.stationService = stationService;
        this.commandFactory = commandFactory;
    }

    @PostMapping("/createOperator")
    public ResponseEntity<OperatorDTO> createRider(@RequestBody OperatorDTO operatorDTO) {
        OperatorDTO createdOperator = operatorService.createOperator(operatorDTO);
        return new ResponseEntity<>(createdOperator, HttpStatus.CREATED);
    }

    //Create DTO
    @PostMapping("/execute")
    public ResponseEntity<String> executeCommand(@RequestBody CommandDTO commandDTO) {

        try {
            Command action = commandFactory.createCommand(commandDTO);
            action.execute();

            return ResponseEntity.ok(commandDTO.getCommand() + " executed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid command: " + commandDTO.getCommand());
        }
    }
}
