package com.veloMTL.veloMTL.Controller.BMSCore;

import com.veloMTL.veloMTL.DTO.Helper.LoyaltyTierDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.veloMTL.veloMTL.Model.Enums.LoyaltyTier;
import com.veloMTL.veloMTL.Service.BMSCore.TierService;

@RestController
@RequestMapping("api/tiers")
public class TierController {
    private final TierService tierService;

    public TierController(TierService tierService) {
        this.tierService = tierService;
    }

    @GetMapping("/{userEmail}")
    public ResponseEntity<LoyaltyTierDTO> getTier(@RequestParam String userEmail) {
        LoyaltyTierDTO DTO = tierService.checkTierChange(userEmail);
        return ResponseEntity.ok(DTO);
    }
}
