package com.veloMTL.veloMTL.Service.PRC;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentMethodListParams;
import com.stripe.param.SetupIntentCreateParams;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Model.Users.User;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentMethodService {

    private final RiderRepository riderRepository;
    private final OperatorRepository operatorRepository;

    // Inject the Stripe secret key from application.properties or environment variable
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    public PaymentMethodService(RiderRepository riderRepository, OperatorRepository operatorRepository) {
        this.riderRepository = riderRepository;
        this.operatorRepository = operatorRepository;
    }


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

    public Map<String, Object> addPaymentMethod(String userEmail) {
        Stripe.apiKey = stripeSecretKey;

        // Fetch the user (Rider or Operator) from the database using their email
        User user = findUserByEmail(userEmail);

        try {
            Customer customer;

            //  create a new Stripe customer if the user has no Stripe ID stored
            if (user.getStripeCustomerId() == null || user.getStripeCustomerId().isEmpty()) {
                System.out.println("Creating new Stripe customer for user: " + userEmail);

                // Create Stripe customer
                CustomerCreateParams customerParams = CustomerCreateParams.builder()
                        .setEmail(user.getEmail())
                        .build();

                customer = Customer.create(customerParams);

                // Save the Stripe customer ID locally to avoid duplicates in future calls
                user.setStripeCustomerId(customer.getId());
                saveUser(user);

                System.out.println("Stripe customer created with ID: " + customer.getId());
            } else {
                System.out.println("Retrieving existing Stripe customer ID: " + user.getStripeCustomerId());

                // Retrieve existing customer from Stripe
                customer = Customer.retrieve(user.getStripeCustomerId());

                // if the Stripe customer was deleted manually.
                if (customer == null) {
                    throw new RuntimeException("Stripe customer not found for ID: " + user.getStripeCustomerId());
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
            response.put("stripeCustomerId", customer.getId()); // Also include as stripeCustomerId for consistency

            System.out.println("SetupIntent created successfully for customer: " + customer.getId());

            return response;

        } catch (Exception e) {
            System.err.println("Failed to add payment method for user: " + userEmail);
            e.printStackTrace();
            throw new RuntimeException("Failed to add payment method: " + e.getMessage());
        }
    }

    public boolean hasStripeCustomerId(String userEmail) {
        User user = findUserByEmail(userEmail);

        return user.getStripeCustomerId() != null && !user.getStripeCustomerId().isEmpty();
    }

    public boolean hasPaymentMethod(String userEmail) {
        Stripe.apiKey = stripeSecretKey;

        User user = findUserByEmail(userEmail);

        // If user doesn't have a Stripe customer ID, they don't have a payment method
        if (user.getStripeCustomerId() == null || user.getStripeCustomerId().isEmpty()) {
            return false;
        }

        try {
            // List payment methods for the customer
            PaymentMethodListParams params = PaymentMethodListParams.builder()
                    .setCustomer(user.getStripeCustomerId())
                    .setType(PaymentMethodListParams.Type.CARD)
                    .build();

            com.stripe.model.PaymentMethodCollection paymentMethods = PaymentMethod.list(params);
            
            // Check if there's at least one payment method
            return paymentMethods != null && paymentMethods.getData() != null && !paymentMethods.getData().isEmpty();
        } catch (Exception e) {
            System.err.println("Failed to check payment methods for user: " + userEmail);
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> getPaymentMethods(String userEmail) {
        Stripe.apiKey = stripeSecretKey;

        User user = findUserByEmail(userEmail);

        // If user doesn't have a Stripe customer ID, return empty list
        if (user.getStripeCustomerId() == null || user.getStripeCustomerId().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // List payment methods for the customer
            PaymentMethodListParams params = PaymentMethodListParams.builder()
                    .setCustomer(user.getStripeCustomerId())
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
            System.err.println("Failed to retrieve payment methods for user: " + userEmail);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public String getStripeCustomerId(String userEmail) {
        User user = findUserByEmail(userEmail);
        return user.getStripeCustomerId();
    }
}


