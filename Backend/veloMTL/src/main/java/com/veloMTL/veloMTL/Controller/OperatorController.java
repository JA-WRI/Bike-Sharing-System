package com.veloMTL.veloMTL.Controller;

import com.veloMTL.veloMTL.DTO.Users.OperatorDTO;
import com.veloMTL.veloMTL.Service.Users.OperatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operators")
public class OperatorController {
    private final OperatorService operatorService;

    public OperatorController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @PostMapping("/createOperator")
    public ResponseEntity<OperatorDTO> createRider(@RequestBody OperatorDTO operatorDTO) {
        OperatorDTO createdOperator = operatorService.createOperator(operatorDTO);
        return new ResponseEntity<>(createdOperator, HttpStatus.CREATED);
    }
}
