package com.veloMTL.veloMTL.Service.PRC;

import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Model.Users.User;
import com.veloMTL.veloMTL.PCR.Billing;
import com.veloMTL.veloMTL.PCR.Strategy.Basic;
import com.veloMTL.veloMTL.PCR.Strategy.Plan;
import com.veloMTL.veloMTL.PCR.Strategy.Premium;
import com.veloMTL.veloMTL.Repository.PRC.BillingRepository;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentPlanService {

    private final RiderRepository riderRepository;
    private final OperatorRepository operatorRepository;
    private final BillingRepository billingRepository;
    private final PaymentMethodService paymentMethodService;

    public PaymentPlanService(RiderRepository riderRepository, OperatorRepository operatorRepository, BillingRepository billingRepository, PaymentMethodService paymentMethodService) {
        this.riderRepository = riderRepository;
        this.operatorRepository = operatorRepository;
        this.billingRepository = billingRepository;
        this.paymentMethodService = paymentMethodService;
    }

    /**
     * Finds a user (Rider or Operator) by email.
     * @param email The email of the user
     * @return The User (Rider or Operator)
     * @throws RuntimeException if user is not found
     */
    private User findUserByEmail(String email) {
        return riderRepository.findByEmail(email)
                .map(rider -> (User) rider)
                .orElseGet(() -> operatorRepository.findByEmail(email)
                        .map(operator -> (User) operator)
                        .orElseThrow(() -> new RuntimeException("User not found with email: " + email)));
    }


    private void saveUser(User user) {
        if (user instanceof Rider) {
            riderRepository.save((Rider) user);
        } else if (user instanceof Operator) {
            operatorRepository.save((Operator) user);
        }
    }

    public String getCurrentPlan(String userEmail) {
        User user = findUserByEmail(userEmail);
        
        Plan plan = user.getPlan();
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

    public Rider addPlan(String userEmail, String chosenPlan){
        // Find user (can be either Rider or Operator)
        User user = findUserByEmail(userEmail);
        
        // First check if user has a Stripe customer ID
        if (!paymentMethodService.hasStripeCustomerId(userEmail)) {
            throw new RuntimeException("Please add a payment method before selecting a plan.");
        }
        
        // Then check if user has at least one payment method attached
        if (!paymentMethodService.hasPaymentMethod(userEmail)) {
            throw new RuntimeException("Please add a payment method before selecting a plan.");
        }
        
        Plan plan = null;
        if(chosenPlan.equalsIgnoreCase("Basic")){
             plan = new Basic();
        } else if(chosenPlan.equalsIgnoreCase("Premium")){
            plan = new Premium();
        } else {
            throw new RuntimeException("Invalid plan selected: " + chosenPlan);
        }
        
        // Create billing record using user's ID (works for both riders and operators)
        Billing billing = new Billing("Monthly Base Fee", LocalDateTime.now(), user.getId(), plan.getBaseFee());
        billingRepository.save(billing);

        // Set plan on user and save
        user.setPlan(plan);
        saveUser(user);
        
        // Return as Rider for backward compatibility
        // If user is an operator, we need to return a Rider type. Since operators can have plans
        // and the controller doesn't use the return value, we can return null safely.
        // However, to avoid potential NPE issues, we'll return the user if it's a Rider, otherwise null.
        if (user instanceof Rider) {
            return (Rider) user;
        } else {
            // For operators, the plan has been saved successfully
            // The controller doesn't use the return value, so returning null is safe
            return null;
        }
    }
}

