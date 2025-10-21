package com.veloMTL.veloMTL.Controller;

import com.veloMTL.veloMTL.DTO.RegistrationDTO;
import com.veloMTL.veloMTL.DTO.auth.AuthResponse;
import com.veloMTL.veloMTL.DTO.auth.AuthUserDTO;
import com.veloMTL.veloMTL.DTO.auth.LoginRequest;
import com.veloMTL.veloMTL.Model.Users.Rider;
import com.veloMTL.veloMTL.Service.Auth.AuthService;
import com.veloMTL.veloMTL.Service.Auth.RegistrationService;
import com.veloMTL.veloMTL.Security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final AuthService authService;
    private final JwtService jwt;

    public AuthController(RegistrationService registrationService,
                          AuthService authService,
                          JwtService jwt){
        this.registrationService = registrationService;
        this.authService = authService;
        this.jwt = jwt;
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

    // ===== REGULAR LOGIN =====
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            // 1) Issue JWT (AuthService already validated credentials)
            String token = authService.loginLocal(req.email(), req.password());

            // 2) Build user info without parsing the token
            AuthUserDTO user = authService.resolveUserByEmail(req.email());

            return ResponseEntity.ok(new AuthResponse(token, user));
        } catch (IllegalArgumentException ex){
            return ResponseEntity.status(401).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", "Unexpected error: " + ex.getMessage()));
        }
    }


    // ===== GOOGLE OAUTH2 SUCCESS HANDLER =====
    @GetMapping("/success")
    public ResponseEntity<?> oauth2Success(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User oAuth2User)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        String email = oAuth2User.getAttribute("email");
        String name  = oAuth2User.getAttribute("name");
        if (email == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Google account missing email"));
        }

        // ensure a Rider exists for this Google account (create-or-find)
        Rider rider = registrationService.registerGoogleUser(name != null ? name : email, email);

        // issue JWT
        String token = jwt.issue(email, Map.of(
                "id", rider.getId(),
                "name", rider.getName(),
                "role", rider.getRole() // "RIDER"
        ));
        AuthUserDTO user = new AuthUserDTO(rider.getId(), rider.getName(), rider.getEmail(), rider.getRole());
        return ResponseEntity.ok(new AuthResponse(token, user));
    }
    // ===== TEMP DEBUG (remove after it works) =====
    @GetMapping("/_debug_login")
    public java.util.Map<String, Object> debugLogin(@RequestParam String email, @RequestParam String pwd) {
        return authService.debugLoginCheck(email, pwd);
    }


}
