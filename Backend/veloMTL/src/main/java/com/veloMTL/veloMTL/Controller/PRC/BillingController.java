package com.veloMTL.veloMTL.Controller.PRC;

import com.veloMTL.veloMTL.PCR.Billing;
import com.veloMTL.veloMTL.Service.PRC.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/list")
    public ResponseEntity<List<Billing>> getAllBillingForRider(@RequestBody Map<String, String> request) {
        String riderEmail = request.get("email");
        List<Billing> bills = billingService.getAllRiderBilling(riderEmail);
        return ResponseEntity.ok(bills);
    }
}

