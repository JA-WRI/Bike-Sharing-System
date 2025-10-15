package com.veloMTL.veloMTL.Controller;

import com.veloMTL.veloMTL.DTO.RiderDTO;
import com.veloMTL.veloMTL.Service.RiderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RiderController {

    private final RiderService riderService; // change to facade obj

    public RiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    @PostMapping("/createRider")
    public ResponseEntity<RiderDTO> createRider(@RequestBody RiderDTO riderDTO) {
        RiderDTO createdRider = riderService.createRider(riderDTO);
        return new ResponseEntity<>(createdRider, HttpStatus.CREATED);
    }
}
