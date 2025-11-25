package com.veloMTL.veloMTL.Service.Users;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SetupIntentCreateParams;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.PCR.Billing;
import com.veloMTL.veloMTL.Repository.PRC.BillingRepository;
import com.veloMTL.veloMTL.PCR.Strategy.Basic;
import com.veloMTL.veloMTL.PCR.Strategy.Plan;
import com.veloMTL.veloMTL.PCR.Strategy.Premium;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class RiderService implements UserDetailsService {

    private final RiderRepository riderRepository;
    private final BillingRepository billingRepository;

    // Inject the Stripe secret key from application.properties or environment variable
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    public RiderService(RiderRepository riderRepository, BillingRepository billingRepository) {
        this.riderRepository = riderRepository;
        this.billingRepository = billingRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Rider rider = riderRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Rider not found with email: " + email));
        return new CustomRiderDetails(rider);
    }

    /**
     * Adds a payment method for the rider.
     * If the rider doesn't have a Stripe customer ID yet, a new customer is created in Stripe
     * and saved to the database.
     */
    public Map<String, Object>  addPaymentMethod(String riderEmail) {
        Stripe.apiKey = stripeSecretKey;

        // Fetch the rider from the database using their email
        Rider rider = riderRepository.findByEmail(riderEmail)
                .orElseThrow(() -> new RuntimeException("Rider not found with email: " + riderEmail));

        try {
            Customer customer;

            //  create a new Stripe customer if the rider has no Stripe ID stored
            if (rider.getStripeCustomerId() == null || rider.getStripeCustomerId().isEmpty()) {
                System.out.println("Creating new Stripe customer for rider: " + riderEmail);

                // Create Stripe customer
                CustomerCreateParams customerParams = CustomerCreateParams.builder()
                        .setEmail(rider.getEmail())
                        .build();

                customer = Customer.create(customerParams);

                // Save the Stripe customer ID locally to avoid duplicates in future calls
                rider.setStripeCustomerId(customer.getId());
                riderRepository.save(rider);

                System.out.println("Stripe customer created with ID: " + customer.getId());
            } else {
                System.out.println("Retrieving existing Stripe customer ID: " + rider.getStripeCustomerId());

                // Retrieve existing customer from Stripe
                customer = Customer.retrieve(rider.getStripeCustomerId());

                // if the Stripe customer was deleted manually.
                if (customer == null) {
                    throw new RuntimeException("Stripe customer not found for ID: " + rider.getStripeCustomerId());
                }
            }

            // Create a new SetupIntent to attach a payment method (like a card)
            SetupIntentCreateParams setupParams = SetupIntentCreateParams.builder()
                    .setCustomer(customer.getId())
                    .addPaymentMethodType("card")
                    .build();

            SetupIntent setupIntent = SetupIntent.create(setupParams);

            Map<String, Object> response = new HashMap<>();
            response.put("clientSecret", setupIntent.getClientSecret());
            response.put("customerId", customer.getId());

            System.out.println("SetupIntent created successfully for customer: " + customer.getId());

            return response;

        } catch (Exception e) {
            System.err.println("Failed to add payment method for rider: " + riderEmail);
            e.printStackTrace();
            throw new RuntimeException("Failed to add payment method: " + e.getMessage());
        }
    }
    public Rider addPlan(String riderEmail, String chosenPlan){
        Rider rider = riderRepository.findByEmail(riderEmail)
                .orElseThrow(() -> new RuntimeException("Rider not found with email: " + riderEmail));
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
