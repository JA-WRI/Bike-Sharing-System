package com.veloMTL.veloMTL.Config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @PostConstruct
    public void init() {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");
        System.out.println("Stripe initialized in test mode!");
    }
}
