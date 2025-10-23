package com.veloMTL.veloMTL.Controller.Users;

import com.veloMTL.veloMTL.DTO.Users.LoginDTO;
import com.veloMTL.veloMTL.DTO.Users.RegistrationDTO;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Security.JwtService;
import com.veloMTL.veloMTL.Service.Auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService){
        this.authService = authService;
        this.jwtService = jwtService;
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

    public ResponseEntity<?> googleLoginRider (@RequestParam String token){

        String email = jwtService.extractEmail(token);
        return ResponseEntity.ok("yooooo you are logged in my dude :p");
    }

    @GetMapping("/dashboard")
    public String loginSuccess() {
        return " Google login successful! Welcome to your dashboard.";
    }
    @PostMapping("/rider/login")
    public ResponseEntity<?> riderLogin(@RequestBody LoginDTO loginDTO){
        String token = authService.loginRider(loginDTO);
        return ResponseEntity.ok(Map.of("token",token));
    }

    @PostMapping("/operator/login")
    public ResponseEntity<?> operatorLogin(@RequestBody LoginDTO loginDTO){
        String token = authService.loginOperator(loginDTO);
        return ResponseEntity.ok(Map.of("token",token));
    }
}
