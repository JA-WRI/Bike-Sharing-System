package com.veloMTL.veloMTL.Controller.BMSCore;

import com.veloMTL.veloMTL.Service.BMSCore.StationService;
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

