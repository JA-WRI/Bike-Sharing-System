package com.veloMTL.veloMTL.Config;

import com.veloMTL.veloMTL.Model.Enums.Permissions;
import com.veloMTL.veloMTL.Repository.Users.OperatorRepository;
import com.veloMTL.veloMTL.Repository.Users.RiderRepository;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
public class SecurityConfig {

    private final GoogleRegistrationService googleRegistrationService;
    private final JwtService jwtService;
    private final JWTFilter jwtFilter;
    private RiderRepository riderRepository;
    private OperatorRepository operatorRepository;

    public SecurityConfig(GoogleRegistrationService googleRegistrationService, JwtService jwtService, JWTFilter jwtFilter, RiderRepository riderRepository, OperatorRepository operatorRepository) {
        this.googleRegistrationService = googleRegistrationService;
        this.jwtService = jwtService;
        this.jwtFilter = jwtFilter;
        this.riderRepository = riderRepository;
        this.operatorRepository = operatorRepository;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"Unauthorized or invalid token\"}");
                        })
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher(request -> !request.getRequestURI().startsWith("/api/"))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login/**",
                                "/oauth2/**",
                                "/stations/**",
                                "/admin/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Return JSON instead of redirect
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"Unauthorized or invalid token\"}");
                        })
                )
                .oauth2Login(oauth2 -> oauth2
                        // This URL is hit *only* when the user clicks "Login with Google"
                        .loginPage("/oauth2/authorization/google")
                        .userInfoEndpoint(userInfo -> userInfo.userService(googleRegistrationService))
                        .successHandler((request, response, authentication) -> {
                            OAuth2User user = (OAuth2User) authentication.getPrincipal();
                            String email = user.getAttribute("email");

                            // Determine role (RIDER or OPERATOR)
                            String role;
                            List<Permissions> permissions;
                            if (riderRepository.existsByEmail(email)) {
                                role = "RIDER";
                                permissions = List.of(Permissions.BIKE_UNLOCK, Permissions.BIKE_RETURN, Permissions.BIKE_RESERVE, Permissions.DOCK_RESERVE);
                            } else if (operatorRepository.existsByEmail(email)) {
                                role = "OPERATOR";
                                permissions =  List.of(Permissions.DOCK_OOS, Permissions.RESTORE_DOCK, Permissions.STATION_OOS, Permissions.RESTORE_STATION, Permissions.BIKE_MOVE);
                            } else {
                                throw new RuntimeException("User not found in either Riders or Operators");
                            }

                            // Generate JWT
                            String token = jwtService.generateToken(email, role, permissions);

                            // Redirect with JWT or return JSON
                            response.sendRedirect("/api/auth/dashboard?token=" + token);
                        })
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}

