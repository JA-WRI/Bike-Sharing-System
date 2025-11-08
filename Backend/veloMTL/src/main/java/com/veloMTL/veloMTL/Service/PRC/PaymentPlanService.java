package com.veloMTL.veloMTL.Service.PRC;

import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.PCR.Billing;
import com.veloMTL.veloMTL.PCR.Strategy.Basic;
import com.veloMTL.veloMTL.PCR.Strategy.Plan;
import com.veloMTL.veloMTL.PCR.Strategy.Premium;
import com.veloMTL.veloMTL.Repository.PRC.BillingRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentPlanService {

    private final RiderRepository riderRepository;
    private final BillingRepository billingRepository;
    private final PaymentMethodService paymentMethodService;

    public PaymentPlanService(RiderRepository riderRepository, BillingRepository billingRepository, PaymentMethodService paymentMethodService) {
        this.riderRepository = riderRepository;
        this.billingRepository = billingRepository;
        this.paymentMethodService = paymentMethodService;
    }

    /**
     * Gets the current plan name for a rider.
     * @param riderEmail The email of the rider
     * @return The plan name ("Basic", "Premium") or null if no plan is set
     */
    public String getCurrentPlan(String riderEmail) {
        Rider rider = riderRepository.findByEmail(riderEmail)
                .orElseThrow(() -> new RuntimeException("Rider not found with email: " + riderEmail));
        
        Plan plan = rider.getPlan();
        if (plan == null) {
            return null;
        }
        
        // Identify plan type by checking the class or base fee
        if (plan instanceof Basic) {
            return "Basic";
        } else if (plan instanceof Premium) {
            return "Premium";
        }
        
        // Fallback: check by base fee
        if (plan.getBaseFee() == 15) {
            return "Basic";
        } else if (plan.getBaseFee() == 20) {
            return "Premium";
        }
        
        return null;
    }

    public Rider addPlan(String riderEmail, String chosenPlan){
        Rider rider = riderRepository.findByEmail(riderEmail)
                .orElseThrow(() -> new RuntimeException("Rider not found with email: " + riderEmail));
        
        // First check if rider has a Stripe customer ID
        if (!paymentMethodService.hasStripeCustomerId(riderEmail)) {
            throw new RuntimeException("Please add a payment method before selecting a plan.");
        }
        
        // Then check if rider has at least one payment method attached
        if (!paymentMethodService.hasPaymentMethod(riderEmail)) {
            throw new RuntimeException("Please add a payment method before selecting a plan.");
        }
        
       Plan plan = null;
        if(chosenPlan.equalsIgnoreCase("Basic")){
             plan = new Basic();
        } else if(chosenPlan.equalsIgnoreCase("Premium")){
            plan = new Premium();
        }
        Billing billing = new Billing("Monthly Base Fee", LocalDateTime.now(), rider.getId(), plan.getBaseFee());
        billingRepository.save(billing);

        rider.setPlan(plan);
        return riderRepository.save(rider);
    }
}

