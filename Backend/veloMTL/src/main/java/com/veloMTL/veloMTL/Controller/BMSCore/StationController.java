package com.veloMTL.veloMTL.Controller.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.BikeDTO;
import com.veloMTL.veloMTL.DTO.BMSCore.StationDTO;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Service.BMSCore.BikeService;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;
    private final BikeService bikeService;

    public StationController(StationService service, BikeService bikeService) {
        this.stationService = service;
        this.bikeService = bikeService;
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<StationDTO> getStationById(@PathVariable String stationId) {
        StationDTO station = stationService.getStationById(stationId);
        return ResponseEntity.ok(station);
    }

    @GetMapping("/{stationId}/bikes")
    public ResponseEntity<List<BikeDTO>> getBikesByStation(@PathVariable String stationId) {
        List<BikeDTO> bikes = bikeService.getBikesByStation(stationId);
        return ResponseEntity.ok(bikes);
    }

}

