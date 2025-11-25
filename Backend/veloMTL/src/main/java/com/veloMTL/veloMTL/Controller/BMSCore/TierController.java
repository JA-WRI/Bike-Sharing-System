package com.veloMTL.veloMTL.Controller.BMSCore;

import com.veloMTL.veloMTL.DTO.Helper.LoyaltyTierDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.veloMTL.veloMTL.Service.BMSCore.TierService;

@RestController
@RequestMapping("/api/tiers")
public class TierController {
    private final TierService tierService;

    public TierController(TierService tierService) {
        this.tierService = tierService;
    }

    @GetMapping("/{userEmail}")
    public ResponseEntity<LoyaltyTierDTO> getTier(@PathVariable String userEmail) {
        // Only return current tier without checking for changes
        // Tier changes are checked and notified only upon login
        LoyaltyTierDTO DTO = tierService.getCurrentTier(userEmail);
        return ResponseEntity.ok(DTO);
    }
}
