package com.veloMTL.veloMTL.Security;

import com.veloMTL.veloMTL.Model.Enums.Permissions;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Use a long, secure secret key. Must be at least 256 bits for HS256
    private final Key SIGNING_KEY = Keys.hmacShaKeyFor("your_super_secure_secret_key_here_which_is_long".getBytes());

    // Generate a token with email and optional extra claims (e.g., role)
    public String generateToken(String email, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hours
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Convenience method to generate token for a specific role
    public String generateToken(String email, String role, List<Permissions> permissions) {
        Map<String, Object> claims = Map.of(
                "role", role,
                "permissions", permissions
        );
        return generateToken(email, claims);
    }

    // Extract email from JWT
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract role from JWT
    public String extractRole(String token) {
        return extractClaim(token, claims -> (String) claims.get("role"));
    }

    // Extract a specific claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired JWT token", e);
        }
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Validate token for a given UserDetails
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractEmail(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
