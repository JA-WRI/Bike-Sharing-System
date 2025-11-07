package com.veloMTL.veloMTL.Controller.Users;

import com.veloMTL.veloMTL.DTO.Helper.CommandDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Patterns.Factory.OperatorCommandFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

            Command<?> action = commandFactory.createCommand(commandDTO, UserStatus.OPERATOR);
            ResponseDTO<?> response = (ResponseDTO<?>) action.execute();
            return ResponseEntity.ok(response);
    }
}
