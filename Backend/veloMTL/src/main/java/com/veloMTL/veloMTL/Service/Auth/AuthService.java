package com.veloMTL.veloMTL.Service.Auth;

import com.veloMTL.veloMTL.DTO.auth.LoginDTO;
import com.veloMTL.veloMTL.DTO.Users.RegistrationDTO;
import com.veloMTL.veloMTL.DTO.auth.LoginResponseDTO;
import com.veloMTL.veloMTL.Model.Users.Operator;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
import com.veloMTL.veloMTL.Security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final RiderRepository riderRepository;
    private final OperatorRepository operatorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(RiderRepository riderRepository, OperatorRepository operatorRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.riderRepository = riderRepository;
        this.operatorRepository = operatorRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // Register a Rider
    public LoginResponseDTO registerRider(RegistrationDTO registrationDTO) {
        if (riderRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already used");
        }
        String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());
        Rider rider = new Rider(
                registrationDTO.getName(),
                registrationDTO.getEmail(),
                encodedPassword
        );

        Rider savedRider = riderRepository.save(rider);
        String token = jwtService.generateToken(rider.getEmail(), rider.getRole(), rider.getPermissions());

        // Return the response including Flex Dollars
        return new LoginResponseDTO(
                token,
                savedRider.getId(),
                savedRider.getName(),
                savedRider.getEmail(),
                savedRider.getRole(),
                savedRider.getFlexDollars()  // Include Flex Dollars in the response
        );
    }

    // Register a Google User (for external login)
    public LoginResponseDTO registerGoogleUser(String name, String email) {
        Optional<Rider> existing = riderRepository.findByEmail(email);

        Rider rider;
        if (existing.isPresent()) {
            rider = existing.get();
        } else {
            rider = new Rider(name, email, null);
            riderRepository.save(rider);
        }

        String token = jwtService.generateToken(rider.getEmail(), rider.getRole(), rider.getPermissions());

        // Return the response including Flex Dollars
        return new LoginResponseDTO(
                token,
                rider.getId(),
                rider.getName(),
                rider.getEmail(),
                rider.getRole(),
                rider.getFlexDollars()  // Include Flex Dollars in the response
        );
    }

    // Login a Rider
    public LoginResponseDTO loginRider(LoginDTO loginDTO) {
        Rider rider = riderRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("wrong email"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), rider.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(rider.getEmail(), rider.getRole(), rider.getPermissions());

        // Return the response including Flex Dollars
        return new LoginResponseDTO(
                token,
                rider.getId(),
                rider.getName(),
                rider.getEmail(),
                rider.getRole(),
                rider.getFlexDollars()  // Include Flex Dollars in the response
        );
    }

    // Login an Operator
    public LoginResponseDTO loginOperator(LoginDTO loginDTO) {
        Operator operator = operatorRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("wrong email"));

        if (!loginDTO.getPassword().equals(operator.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(operator.getEmail(), operator.getRole(), operator.getPermissions());

        // Return the response for Operator (No Flex Dollars for operator)
        return new LoginResponseDTO(
                token,
                operator.getId(),
                operator.getName(),
                operator.getEmail(),
                operator.getRole(),
                0.0  // Operator doesn't have Flex Dollars
        );
    }
}
