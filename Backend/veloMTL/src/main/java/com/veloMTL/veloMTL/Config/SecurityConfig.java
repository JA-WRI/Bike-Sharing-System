package com.veloMTL.veloMTL.Config;
import com.veloMTL.veloMTL.Service.Auth.GoogleRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final GoogleRegistrationService googleRegistrationService;


    public SecurityConfig(GoogleRegistrationService googleRegistrationService) {
        this.googleRegistrationService = googleRegistrationService;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/register",   // normal registration
                                "/api/auth/",         // all auth endpoints
                                "/oauth2/",           // google oauth entry
                                "/login/**"             // google oauth redirect
                        ).permitAll()
                        .anyRequest().authenticated() // everything else requires auth
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(googleRegistrationService) // tell Spring to use your service
                        )
                        .defaultSuccessUrl("/api/auth/success", true) // redirect after successful login
                );

        return http.build();
    }
}

