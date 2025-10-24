package com.veloMTL.veloMTL.Security;

import com.veloMTL.veloMTL.Service.Users.OperatorService;
import com.veloMTL.veloMTL.Service.Users.RiderService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RiderService riderService;
    private final OperatorService operatorService;

    public JWTFilter(JwtService jwtService, RiderService riderService, OperatorService operatorService) {
        this.jwtService = jwtService;
        this.riderService = riderService;
        this.operatorService = operatorService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Remove "Bearer "
            username = jwtService.extractEmail(jwt); // extract email/username from JWT
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Extract role from JWT
            String role = jwtService.extractRole(jwt);
            UserDetails userDetails;

            if ("RIDER".equals(role)) {
                userDetails = riderService.loadUserByUsername(username);
            } else if ("OPERATOR".equals(role)) {
                userDetails = operatorService.loadUserByUsername(username);
            } else {
                throw new RuntimeException("Unknown role in JWT: " + role);
            }

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
