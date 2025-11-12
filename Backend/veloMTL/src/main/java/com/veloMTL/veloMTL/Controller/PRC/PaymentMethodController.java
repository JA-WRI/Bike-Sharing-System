package com.veloMTL.veloMTL.Controller.PRC;

import com.veloMTL.veloMTL.Service.PRC.PaymentMethodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addPaymentMethod(@RequestBody Map<String, String> request) {
        String riderEmail = request.get("email");
        Map<String, Object> response = paymentMethodService.addPaymentMethod(riderEmail);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkPaymentMethod(@RequestBody Map<String, String> request) {
        String riderEmail = request.get("email");
        boolean hasPaymentMethod = paymentMethodService.hasPaymentMethod(riderEmail);
        Map<String, Boolean> response = new java.util.HashMap<>();
        response.put("hasPaymentMethod", hasPaymentMethod);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check-customer-id")
    public ResponseEntity<Map<String, Boolean>> checkStripeCustomerId(@RequestBody Map<String, String> request) {
        String riderEmail = request.get("email");
        boolean hasStripeCustomerId = paymentMethodService.hasStripeCustomerId(riderEmail);
        Map<String, Boolean> response = new java.util.HashMap<>();
        response.put("hasStripeCustomerId", hasStripeCustomerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> getPaymentMethods(@RequestBody Map<String, String> request) {
        String riderEmail = request.get("email");
        List<Map<String, Object>> paymentMethods = paymentMethodService.getPaymentMethods(riderEmail);
        return ResponseEntity.ok(paymentMethods);
    }
}

