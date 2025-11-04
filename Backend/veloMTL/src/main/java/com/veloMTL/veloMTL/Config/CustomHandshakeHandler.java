package com.veloMTL.veloMTL.Config;

import com.veloMTL.veloMTL.Security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtService jwtService;

    public CustomHandshakeHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        URI uri = request.getURI();
        String query = uri.getQuery(); // e.g., token=xxx
        if (query == null) return null;

        String token = Arrays.stream(query.split("&"))
                .filter(s -> s.startsWith("token="))
                .map(s -> s.substring(6))
                .findFirst().orElse(null);

        if (token != null) {
            try {
                String email = jwtService.extractEmail(token);
                // Optionally, validate expiration
                return () -> email; // Spring Security sees this as the authenticated principal
            } catch (Exception e) {
                System.out.println("Invalid token for WebSocket: " + e.getMessage());
            }
        }
        return null;
    }
}
