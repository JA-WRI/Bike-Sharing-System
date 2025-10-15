package com.veloMTL.veloMTL.Controller;

import com.veloMTL.veloMTL.DTO.RegistrationDTO;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Service.Auth.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationService registrationService;

    public AuthController(RegistrationService registrationService){
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerRider (@RequestBody RegistrationDTO registrationDTO){
        try {
            Rider rider = registrationService.registerRider(registrationDTO);
            return ResponseEntity.ok("Rider registered successfully with email: " + rider.getEmail());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
