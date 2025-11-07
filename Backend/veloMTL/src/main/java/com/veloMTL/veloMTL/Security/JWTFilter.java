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
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
                username = jwtService.extractEmail(jwt);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
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

            // Continue chain (even if controller throws)
            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            sendUnauthorized(response, "Token expired");
        } catch (io.jsonwebtoken.SignatureException e) {
            sendUnauthorized(response, "Invalid token signature");
        } catch (io.jsonwebtoken.JwtException e) {
            sendUnauthorized(response, "Invalid or malformed token");
        }
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
