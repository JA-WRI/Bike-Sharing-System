package com.veloMTL.veloMTL.Controller.Users;

import com.veloMTL.veloMTL.DTO.Helper.CommandDTO;
import com.veloMTL.veloMTL.DTO.Helper.ResponseDTO;
import com.veloMTL.veloMTL.Model.Enums.UserStatus;
import com.veloMTL.veloMTL.PCR.Billing;
import com.veloMTL.veloMTL.PCR.BillingService;
import com.veloMTL.veloMTL.Patterns.Command.Command;
import com.veloMTL.veloMTL.Patterns.Factory.RiderCommandFactory;
import com.veloMTL.veloMTL.Service.Users.RiderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/riders")
public class RiderController {

    private final RiderService riderService; // change to facade obj
    private final RiderCommandFactory commandFactory;
    private final BillingService billingService;
    public RiderController(RiderService riderService, RiderCommandFactory commandFactory, BillingService billingService) {
        this.riderService = riderService;
        this.commandFactory = commandFactory;
        this.billingService = billingService;
    }

    @PostMapping("/command")
    public ResponseEntity<ResponseDTO<?>> executeCommand(@RequestBody CommandDTO commandDTO){

        Command<?> action = commandFactory.createCommand(commandDTO,UserStatus.RIDER );
        ResponseDTO<?> response = (ResponseDTO<?>) action.execute();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/addPaymentMethod")
    public ResponseEntity<Map<String, Object>> addPaymentMethod(@RequestBody Map<String, String> request) {
        String riderEmail = request.get("email");
        Map<String, Object> response = riderService.addPaymentMethod(riderEmail);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-plan")
    public ResponseEntity<String> addPlan(@RequestBody Map<String, String> request) {
        try {
            String riderEmail = request.get("email");
            String plan = request.get("plan");

            riderService.addPlan(riderEmail, plan);
            return ResponseEntity.ok("Plan added successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error adding plan: " + e.getMessage());
        }
    }
    @GetMapping("/{riderID}/billing")
    public ResponseEntity<List<Billing>> getAllBillingForRider(@PathVariable String riderID) {
        List<Billing> bills = billingService.getAllRiderBilling(riderID);
        return ResponseEntity.ok(bills);
    }
}
