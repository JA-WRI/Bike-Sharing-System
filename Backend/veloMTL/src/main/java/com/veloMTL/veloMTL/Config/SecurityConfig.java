package com.veloMTL.veloMTL.Config;

import com.veloMTL.veloMTL.Security.JWTFilter;
import com.veloMTL.veloMTL.Security.JwtService;
import com.veloMTL.veloMTL.Service.Auth.GoogleRegistrationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
    @Order(1)
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        System.out.println(">>> \n\n\n\n\n\n\nAPI SECURITY FILTER CHAIN LOADED\n\n\n\n\n\n\n");
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/rider/login",
                                "/api/auth/operator/login",
                                "/api/auth/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("Unauthorized or invalid token");
                        })
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        http
                //Only match non-API requests
                .securityMatcher(request -> !request.getRequestURI().startsWith("/api/"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login/**", "/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")
                        .userInfoEndpoint(userInfo -> userInfo.userService(googleRegistrationService))
                        .successHandler((request, response, authentication) -> {
                            OAuth2User user = (OAuth2User) authentication.getPrincipal();
                            String email = user.getAttribute("email");

                            String token = jwtService.generateToken(email);
                            response.sendRedirect("/api/auth/dashboard?token=" + token);
                        })
                );

        return http.build();
    }

}

