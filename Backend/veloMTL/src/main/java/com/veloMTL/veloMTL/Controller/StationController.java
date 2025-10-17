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
    //Add get requests to get the stations

}

