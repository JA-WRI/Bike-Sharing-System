package com.veloMTL.veloMTL.Controller.Users;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.BMSCore.DockDTO;
import com.veloMTL.veloMTL.DTO.BMSCore.StationDTO;
import com.veloMTL.veloMTL.DTO.Users.CreateOperatorDTO;
import com.veloMTL.veloMTL.DTO.Users.OperatorDTO;
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;
import com.veloMTL.veloMTL.Service.Users.OperatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")

public class AdminController {
    private final OperatorService operatorService;
    private final DockService dockService;
    private final StationService stationService;
    private final BikeService bikeService;

    public AdminController(OperatorService operatorService, DockService dockService, StationService stationService, BikeService bikeService) {
        this.operatorService = operatorService;
        this.dockService = dockService;
        this.stationService = stationService;
        this.bikeService = bikeService;
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
    @PostMapping("/createStation")
    public ResponseEntity<StationDTO> createStation(@RequestBody StationDTO stationDTO) {
        StationDTO createdStation = stationService.createStation(stationDTO);
        return new ResponseEntity<>(createdStation, HttpStatus.CREATED);
    }
    @PostMapping("/createBike")
    public ResponseEntity<BikeDTO> createStation(@RequestBody BikeDTO bikeDTO) {
        BikeDTO createdBike = bikeService.createBike(bikeDTO);
        return new ResponseEntity<>(createdBike, HttpStatus.CREATED);
    }

}
