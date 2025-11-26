package com.veloMTL.veloMTL.Controller.PRC;

import com.veloMTL.veloMTL.Service.PRC.PaymentPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment-plans")
public class PaymentPlanController {

    private final PaymentPlanService paymentPlanService;

    public PaymentPlanController(PaymentPlanService paymentPlanService) {
        this.paymentPlanService = paymentPlanService;
    }

    @PostMapping("/get")
    public ResponseEntity<Map<String, String>> getPlan(@RequestBody Map<String, String> request) {
        String riderEmail = request.get("email");
        String planName = paymentPlanService.getCurrentPlan(riderEmail);
        Map<String, String> response = new java.util.HashMap<>();
        response.put("plan", planName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPlan(@RequestBody Map<String, String> request) {
        try {
            String riderEmail = request.get("email");
            String plan = request.get("plan");

            paymentPlanService.addPlan(riderEmail, plan);
            return ResponseEntity.ok("Plan added successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error adding plan: " + e.getMessage());
        }
    }
}

