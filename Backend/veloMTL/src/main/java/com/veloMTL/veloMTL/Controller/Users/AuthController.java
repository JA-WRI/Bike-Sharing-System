package com.veloMTL.veloMTL.Controller.Users;

import com.veloMTL.veloMTL.DTO.auth.LoginDTO;
import com.veloMTL.veloMTL.DTO.Users.RegistrationDTO;
import com.veloMTL.veloMTL.DTO.auth.LoginResponseDTO;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Service.Auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerRider (@RequestBody RegistrationDTO registrationDTO){
        try {
            Rider rider = authService.registerRider(registrationDTO);
            return ResponseEntity.ok("Rider registered successfully with email: " + rider.getEmail());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/dashboard")
    public String loginSuccess() {
        return " Google login successful! Welcome to your dashboard.";
    }

    @PostMapping("/rider/login")
    public ResponseEntity<?> riderLogin(@RequestBody LoginDTO loginDTO){
        LoginResponseDTO response = authService.loginRider(loginDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/operator/login")
    public ResponseEntity<?> operatorLogin(@RequestBody LoginDTO loginDTO){
        LoginResponseDTO response = authService.loginOperator(loginDTO);
        return ResponseEntity.ok(response);
    }
}
