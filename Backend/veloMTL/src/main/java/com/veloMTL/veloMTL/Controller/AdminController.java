package com.veloMTL.veloMTL.Controller;

import com.veloMTL.veloMTL.DTO.DockDTO;
import com.veloMTL.veloMTL.DTO.StationDTO;
import com.veloMTL.veloMTL.DTO.Users.CreateOperatorDTO;
import com.veloMTL.veloMTL.DTO.Users.OperatorDTO;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;
import com.veloMTL.veloMTL.Service.Users.OperatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")

public class AdminController {
    private final OperatorService operatorService;
    private final DockService dockService;
    private final StationService stationService;

    public AdminController(OperatorService operatorService, DockService dockService, StationService stationService) {
        this.operatorService = operatorService;
        this.dockService = dockService;
        this.stationService = stationService;
    }

    @PostMapping("/createOperator")
    public ResponseEntity<OperatorDTO> createRider(@RequestBody CreateOperatorDTO operator) {
        OperatorDTO createdOperator = operatorService.createOperator(operator);
        return new ResponseEntity<>(createdOperator, HttpStatus.CREATED);
    }

    @PostMapping("/createDock")
    public ResponseEntity<DockDTO> createDock(@RequestBody DockDTO dockDTO) {

        DockDTO savedDock = dockService.createDock(dockDTO);
        return new ResponseEntity<>(savedDock, HttpStatus.CREATED);
    }
    @PostMapping
    public ResponseEntity<StationDTO> createStation(@RequestBody StationDTO stationDTO) {
        StationDTO createdStation = stationService.createStation(stationDTO);
        return new ResponseEntity<>(createdStation, HttpStatus.CREATED);
    }

}
