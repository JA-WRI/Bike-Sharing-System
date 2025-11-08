package com.veloMTL.veloMTL.Service.PRC;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentMethodListParams;
import com.stripe.param.SetupIntentCreateParams;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentMethodService {

    private final RiderRepository riderRepository;

    // Inject the Stripe secret key from application.properties or environment variable
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    public PaymentMethodService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    /**
     * Adds a payment method for the rider.
     * If the rider doesn't have a Stripe customer ID yet, a new customer is created in Stripe
     * and saved to the database.
     */
    public Map<String, Object> addPaymentMethod(String riderEmail) {
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

    /**
     * Checks if a rider has a Stripe customer ID.
     * @param riderEmail The email of the rider
     * @return true if the rider has a Stripe customer ID, false otherwise
     */
    public boolean hasStripeCustomerId(String riderEmail) {
        Rider rider = riderRepository.findByEmail(riderEmail)
                .orElseThrow(() -> new RuntimeException("Rider not found with email: " + riderEmail));

        return rider.getStripeCustomerId() != null && !rider.getStripeCustomerId().isEmpty();
    }

    /**
     * Checks if a rider has at least one payment method attached to their Stripe customer.
     * @param riderEmail The email of the rider
     * @return true if the rider has at least one payment method, false otherwise
     */
    public boolean hasPaymentMethod(String riderEmail) {
        Stripe.apiKey = stripeSecretKey;

        Rider rider = riderRepository.findByEmail(riderEmail)
                .orElseThrow(() -> new RuntimeException("Rider not found with email: " + riderEmail));

        // If rider doesn't have a Stripe customer ID, they don't have a payment method
        if (rider.getStripeCustomerId() == null || rider.getStripeCustomerId().isEmpty()) {
            return false;
        }

        try {
            // List payment methods for the customer
            PaymentMethodListParams params = PaymentMethodListParams.builder()
                    .setCustomer(rider.getStripeCustomerId())
                    .setType(PaymentMethodListParams.Type.CARD)
                    .build();

            com.stripe.model.PaymentMethodCollection paymentMethods = PaymentMethod.list(params);
            
            // Check if there's at least one payment method
            return paymentMethods != null && paymentMethods.getData() != null && !paymentMethods.getData().isEmpty();
        } catch (Exception e) {
            System.err.println("Failed to check payment methods for rider: " + riderEmail);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all payment methods for a rider with card details.
     * @param riderEmail The email of the rider
     * @return List of maps containing payment method details (card brand, last4, exp_month, exp_year, name)
     */
    public List<Map<String, Object>> getPaymentMethods(String riderEmail) {
        Stripe.apiKey = stripeSecretKey;

        Rider rider = riderRepository.findByEmail(riderEmail)
                .orElseThrow(() -> new RuntimeException("Rider not found with email: " + riderEmail));

        // If rider doesn't have a Stripe customer ID, return empty list
        if (rider.getStripeCustomerId() == null || rider.getStripeCustomerId().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // List payment methods for the customer
            PaymentMethodListParams params = PaymentMethodListParams.builder()
                    .setCustomer(rider.getStripeCustomerId())
                    .setType(PaymentMethodListParams.Type.CARD)
                    .build();

            com.stripe.model.PaymentMethodCollection paymentMethods = PaymentMethod.list(params);
            
            List<Map<String, Object>> result = new ArrayList<>();
            
            if (paymentMethods != null && paymentMethods.getData() != null) {
                for (PaymentMethod pm : paymentMethods.getData()) {
                    Map<String, Object> cardInfo = new HashMap<>();
                    cardInfo.put("id", pm.getId());
                    
                    // Get card details
                    if (pm.getCard() != null) {
                        cardInfo.put("brand", pm.getCard().getBrand());
                        cardInfo.put("last4", pm.getCard().getLast4());
                        cardInfo.put("expMonth", pm.getCard().getExpMonth());
                        cardInfo.put("expYear", pm.getCard().getExpYear());
                    }
                    
                    // Get billing details (name)
                    if (pm.getBillingDetails() != null) {
                        cardInfo.put("name", pm.getBillingDetails().getName());
                    }
                    
                    result.add(cardInfo);
                }
            }
            
            return result;
        } catch (Exception e) {
            System.err.println("Failed to retrieve payment methods for rider: " + riderEmail);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

