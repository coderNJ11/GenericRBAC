package com.example.demo;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

public class JwtTokenUtils {

    private final JwtDecoder jwtDecoder;

    // Constructor accepts a secret key for token decoding
    public JwtTokenUtils(String secretKey) {
        // Use the secret key to configure the NimbusJwtDecoder
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
    }

    // Method to validate and decode a token
    public Jwt decodeToken(String token) {
        try {
            // Remove "Bearer " prefix from token, if present
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token: " + e.getMessage(), e);
        }
    }

    // Method to extract claims from the decoded token
    public Map<String, Object> parseToken(String token) {
        Jwt jwt = decodeToken(token);
        return jwt.getClaims();
    }

    // Method to check if the token has a specified access role or permission
    public boolean hasAccess(Jwt jwt, String requiredRole) {
        // Extract 'scope' claim, which contains permissions or roles
        String roles = jwt.getClaimAsString("scope");
        if (roles == null) {
            return false;
        }

        // Check if the required role exists within the extracted roles
        return roles.contains(requiredRole);
    }
}