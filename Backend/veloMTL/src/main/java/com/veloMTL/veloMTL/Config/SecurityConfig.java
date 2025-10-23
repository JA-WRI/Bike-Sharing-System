package com.veloMTL.veloMTL.Config;

import com.veloMTL.veloMTL.Security.JWTFilter;
import com.veloMTL.veloMTL.Security.JwtService;
import com.veloMTL.veloMTL.Service.Auth.GoogleRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Configuration
public class SecurityConfig {

    private final GoogleRegistrationService googleRegistrationService;
    private final JwtService jwtService;
    private final JWTFilter jwtFilter;

    public SecurityConfig(GoogleRegistrationService googleRegistrationService, JwtService jwtService, JWTFilter jwtFilter) {
        this.googleRegistrationService = googleRegistrationService;
        this.jwtService = jwtService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/register",       // public registration
                                "/api/auth/rider/login",    // public regular login
                                "/api/auth/operator/login",
                                "/api/auth/",               // general auth endpoints
                                "/oauth2/",                 // google oauth entry
                                "/login/**"                 // google oauth redirect
                        ).permitAll()
                        .anyRequest().authenticated() // everything else needs auth
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")
                        .userInfoEndpoint(userInfo -> userInfo.userService(googleRegistrationService))
                        .successHandler((request, response, authentication) -> {
                            OAuth2User user = (OAuth2User) authentication.getPrincipal();
                            String email = user.getAttribute("email");
                            String name = user.getAttribute("name");

                            // generate JWT for Google login
                            String token = jwtService.generateToken(email);

                            // return token in JSON for API or redirect to dashboard
                            response.sendRedirect("/api/auth/dashboard?token=" + token);
                        })
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
