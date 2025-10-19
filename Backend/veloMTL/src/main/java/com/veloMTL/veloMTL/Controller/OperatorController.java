package com.veloMTL.veloMTL.Controller;

import com.veloMTL.veloMTL.DTO.CommandDTO;
import com.veloMTL.veloMTL.DTO.DockDTO;
import com.veloMTL.veloMTL.DTO.ResponseDTO;
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

    private final OperatorCommandFactory commandFactory;

    public OperatorController(OperatorCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    @PostMapping("/command")
    public ResponseEntity<ResponseDTO<?>> executeCommand(
            @RequestBody CommandDTO commandDTO) {

            Command<?> action = commandFactory.createCommand(commandDTO);
            ResponseDTO<?> response = (ResponseDTO<?>) action.execute();
            return ResponseEntity.ok(response);
    }
}
