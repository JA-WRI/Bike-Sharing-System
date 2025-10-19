package com.veloMTL.veloMTL.Controller;

import com.veloMTL.veloMTL.DTO.DockDTO;
import com.veloMTL.veloMTL.DTO.StationDTO;
import com.veloMTL.veloMTL.DTO.Users.OperatorDTO;
import com.veloMTL.veloMTL.Model.Enums.DockStatus;
import com.veloMTL.veloMTL.Model.Enums.StationStatus;
import com.veloMTL.veloMTL.Service.BMSCore.DockService;
import com.veloMTL.veloMTL.Service.BMSCore.StationService;
import com.veloMTL.veloMTL.Service.Users.OperatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/operators")
public class OperatorController {
    private final OperatorService operatorService;
    private final DockService dockService;
    private final StationService stationService;

    public OperatorController(OperatorService operatorService, DockService dockService, StationService stationService) {
        this.operatorService = operatorService;
        this.dockService = dockService;
        this.stationService = stationService;
    }

    @PostMapping("/createOperator")
    public ResponseEntity<OperatorDTO> createRider(@RequestBody OperatorDTO operatorDTO) {
        OperatorDTO createdOperator = operatorService.createOperator(operatorDTO);
        return new ResponseEntity<>(createdOperator, HttpStatus.CREATED);
    }

    @PatchMapping("/docks/{dockId}/status")
    public ResponseEntity<String> updateDockStatus(
            @PathVariable String dockId,
            @RequestParam DockStatus status) {

        DockDTO updated = dockService.updateDockStatus(dockId, status);
        return new ResponseEntity<>("Dock status updated successfully.", HttpStatus.OK);
    }
    @PutMapping("/stations/{stationId}/status")
    public ResponseEntity<StationDTO> updateStationStatus(
            @PathVariable String stationId,
            @RequestBody Map<String, String> requestBody) {
        String status = requestBody.get("status");

        try {
            // Convert the string (e.g., "OUT_OF_SERVICE") to the enum
            StationStatus newStatus = StationStatus.valueOf(status.toUpperCase());

            StationDTO updatedStation = stationService.updateStationStatus(stationId, newStatus);
            return new ResponseEntity<>(updatedStation, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            // If the string doesn’t match any enum constant
            return ResponseEntity.badRequest().build();
        }
    }


}
