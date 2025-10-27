package com.veloMTL.veloMTL.Controller.BMSCore;

import com.veloMTL.veloMTL.DTO.BMSCore.StationDTO;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService service) {
        this.stationService = service;
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<StationDTO> getStationById(@PathVariable String stationId) {
        StationDTO station = stationService.getStationById(stationId);
        return ResponseEntity.ok(station);
    }
}

