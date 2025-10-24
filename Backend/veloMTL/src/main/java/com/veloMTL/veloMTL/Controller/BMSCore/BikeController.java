package com.veloMTL.veloMTL.Controller.BMSCore;

import com.veloMTL.veloMTL.Service.BMSCore.BikeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bikes")
public class BikeController {
    private final BikeService bikeService;

    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }
}
