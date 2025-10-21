package com.veloMTL.veloMTL.Controller;

import com.veloMTL.veloMTL.DTO.DockDTO;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/docks")
public class DockController {
    private final DockService dockService;

    public DockController(DockService dockService){
        this.dockService = dockService;
    }

    @PostMapping("/{stationId}")
    public ResponseEntity<DockDTO> createDock(
            @PathVariable String stationId,
            @RequestBody DockDTO dockDTO) {

        DockDTO savedDock = dockService.createDock(stationId, dockDTO);
        return new ResponseEntity<>(savedDock, HttpStatus.CREATED);
    }

}
