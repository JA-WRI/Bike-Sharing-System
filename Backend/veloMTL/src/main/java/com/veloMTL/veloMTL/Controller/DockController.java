package com.veloMTL.veloMTL.Controller;

import com.veloMTL.veloMTL.Service.BMSCore.DockService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/docks")
public class DockController {
    private final DockService dockService;

    public DockController(DockService dockService){
        this.dockService = dockService;
    }
    //Add get request to get the docks


}
