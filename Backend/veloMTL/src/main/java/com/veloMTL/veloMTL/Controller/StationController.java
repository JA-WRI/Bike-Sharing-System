package com.veloMTL.veloMTL.Controller;


import com.veloMTL.veloMTL.DTO.StationDTO;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService service) {
        this.stationService = service;
    }
    @PostMapping
    public ResponseEntity<StationDTO> createStation(@RequestBody StationDTO stationDTO) {
        StationDTO createdStation = stationService.createStation(stationDTO);
        return new ResponseEntity<>(createdStation, HttpStatus.CREATED);
    }

}

