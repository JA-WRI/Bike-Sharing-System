package com.veloMTL.veloMTL.Service.Auth;

import com.veloMTL.veloMTL.DTO.RegistrationDTO;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.RiderRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Service
public class RegistrationService {
    private final RiderRepository riderRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(RiderRepository riderRepository, PasswordEncoder passwordEncoder){
        this.riderRepository = riderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Rider registerRider (RegistrationDTO registrationDTO){
    if(riderRepository.existsByEmail(registrationDTO.getEmail())){
        throw  new RuntimeException("Email already used");
    }
     String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());
    Rider rider = new Rider(
            registrationDTO.getName(),
            registrationDTO.getEmail(),
            encodedPassword
    );
        return riderRepository.save(rider);
    }

    public Rider registerGoogleUser(String name, String email) {
        Optional<Rider> existing = riderRepository.findByEmail(email);

        if (existing.isPresent()) {
            return existing.get();
        }

        Rider newRider = new Rider(
        name,
        email,
        null);
        return riderRepository.save(newRider);
    }
}
