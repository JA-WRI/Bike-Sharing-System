package com.veloMTL.veloMTL.Service.Users;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SetupIntentCreateParams;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RiderService implements UserDetailsService {

    private final RiderRepository riderRepository;

    // Inject Stripe secret key from environment
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Rider rider = riderRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Rider not found with email: " + email));
        return new CustomRiderDetails(rider);
    }

    public Map<String, Object> addPaymentMethod(String riderEmail) {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY"); // Or read from application.properties

        Rider rider = riderRepository.findByEmail(riderEmail)
                .orElseThrow(() -> new RuntimeException("Rider not found with email: " + riderEmail));

        try {
            Customer customer;
            if (rider.getStripeCustomerId() == null) {
                CustomerCreateParams customerParams = CustomerCreateParams.builder()
                        .setEmail(rider.getEmail())
                        .build();
                customer = Customer.create(customerParams);
                rider.setStripeCustomerId(customer.getId());
                riderRepository.save(rider);
            } else {
                customer = Customer.retrieve(rider.getStripeCustomerId());
            }

            SetupIntentCreateParams setupParams = SetupIntentCreateParams.builder()
                    .setCustomer(customer.getId())
                    .addPaymentMethodType("card")
                    .build();
            SetupIntent setupIntent = SetupIntent.create(setupParams);

            Map<String, Object> response = new HashMap<>();
            response.put("clientSecret", setupIntent.getClientSecret());
            response.put("customerId", customer.getId());
            return response;

        } catch (Exception e) {
            throw new RuntimeException("Failed to add payment method: " + e.getMessage());
        }
    }

}
