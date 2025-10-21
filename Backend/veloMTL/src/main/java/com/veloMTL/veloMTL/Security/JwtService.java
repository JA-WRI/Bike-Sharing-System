package com.veloMTL.veloMTL.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;                 // <-- use SecretKey
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final SecretKey key;              // <-- SecretKey
    private final long ttlMs = 1000L * 60 * 60 * 12; // 12 hours

    public JwtService(@Value("${jwt.secret:CHANGEME_64+}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String issue(String subjectEmail, Map<String, Object> claims) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(subjectEmail)
                .claims(claims)
                .issuedAt(new Date(now))
                .expiration(new Date(now + ttlMs))
                .signWith(key)                  // HS256 chosen automatically for HMAC keys
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)                // now matches SecretKey overload
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
