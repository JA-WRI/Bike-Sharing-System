package com.veloMTL.veloMTL.Service.Auth;

<<<<<<< HEAD
import com.veloMTL.veloMTL.DTO.Users.RegistrationDTO;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
=======
import com.veloMTL.veloMTL.DTO.RegistrationDTO;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.RiderRepository;
>>>>>>> uroosa2
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

<<<<<<< HEAD

@Service
public class RegistrationService {
    private final RiderRepository riderRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(RiderRepository riderRepository, PasswordEncoder passwordEncoder){
=======
@Service
public class RegistrationService {

    private final RiderRepository riderRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(RiderRepository riderRepository,
                               PasswordEncoder passwordEncoder) {
>>>>>>> uroosa2
        this.riderRepository = riderRepository;
        this.passwordEncoder = passwordEncoder;
    }

<<<<<<< HEAD
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

=======
    /** Local registration (email + password). Password is BCrypt hashed. */
    public Rider registerRider(RegistrationDTO dto) {
        if (riderRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        String hashed = passwordEncoder.encode(dto.getPassword());

        Rider rider = new Rider();
        rider.setName(dto.getName());
        rider.setEmail(dto.getEmail());
        rider.setPassword(hashed);
        rider.setRole("RIDER"); // adjust if your model uses an enum/value

        return riderRepository.save(rider);
    }

    /** Google registration (no password). Creates the rider if not present. */
    public Rider registerGoogleUser(String name, String email) {
        Optional<Rider> existing = riderRepository.findByEmail(email);
>>>>>>> uroosa2
        if (existing.isPresent()) {
            return existing.get();
        }

<<<<<<< HEAD
        Rider newRider = new Rider(
        name,
        email,
        null);
        return riderRepository.save(newRider);
=======
        Rider rider = new Rider();
        rider.setName(name != null ? name : email);
        rider.setEmail(email);
        rider.setPassword(null);     // no local password for Google users
        rider.setRole("RIDER");

        return riderRepository.save(rider);
>>>>>>> uroosa2
    }
}
