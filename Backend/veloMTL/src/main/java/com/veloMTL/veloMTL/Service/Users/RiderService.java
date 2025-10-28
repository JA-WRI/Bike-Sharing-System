package com.veloMTL.veloMTL.Service.Users;

import com.veloMTL.veloMTL.DTO.BMSCore.PaymentMethodDTO;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Patterns.Strategy.MasterCard;
import com.veloMTL.veloMTL.Patterns.Strategy.PaymentMethod;
import com.veloMTL.veloMTL.Patterns.Strategy.Paypal;
import com.veloMTL.veloMTL.Patterns.Strategy.Visa;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RiderService implements UserDetailsService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Rider rider = riderRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Rider not found with email: " + email));
        return new CustomRiderDetails(rider); // Wrap Rider into UserDetails
    }
    public String addPaymentMethod(PaymentMethodDTO paymentMethodDTO){
        //get email of the current user logged in
        String riderEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Rider rider = riderRepository.findByEmail(riderEmail)
                .orElseThrow(() -> new RuntimeException("Rider not found with email: " + riderEmail));
        PaymentMethod paymentMethod = createPaymentMethod(paymentMethodDTO);
        rider.setPaymentMethod(paymentMethod);
        riderRepository.save(rider);
        return "Payment method added for rider: " + rider.getName();
    }
    private PaymentMethod createPaymentMethod(PaymentMethodDTO paymentMethodDTO){
        return switch (paymentMethodDTO.getPaymentMethod().toUpperCase()) {
            case "VISA" -> new Visa(); // Example: Visa with card number
            case "MASTERCARD" -> new MasterCard();
            case "PAYPAL" -> new Paypal();
            default ->
                    throw new IllegalArgumentException("Unsupported payment method: " + paymentMethodDTO.getPaymentMethod());
        };
    }

}
