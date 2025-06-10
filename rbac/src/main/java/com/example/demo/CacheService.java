package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cache")
public class CacheService {

    @Autowired
    private CacheManager cacheManager; // Assume Redis or another cache implementation

    @Autowired
    private JwtTokenUtils jwtTokenUtils; // JwtTokenUtils is injected as a managed bean

    @PostMapping("/{cacheName}")
    public ResponseEntity<String> storeData(
            @PathVariable String cacheName,
            @RequestBody Map<String, Object> data,
            @RequestHeader("Authorization") String token) {

        try {
            // Extract and validate the token
            Jwt tokenDetails = jwtTokenUtils.decodeToken(token);

            // Check if the user has permission to access the cache
            if (!jwtTokenUtils.hasAccess(tokenDetails, cacheName.toUpperCase() + "_ACCESS")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
            }

            // Store data in the specified cache
            cacheManager.store(cacheName, data);

            return ResponseEntity.ok("Data stored successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: " + e.getMessage());
        }
    }

    @GetMapping("/{cacheName}")
    public ResponseEntity<Map<String, Object>> getData(
            @PathVariable String cacheName,
            @RequestHeader("Authorization") String token) {

        try {
            // Extract and validate the token
            Jwt tokenDetails = jwtTokenUtils.decodeToken(token);

            // Check if the user has permission to access the cache
            if (!jwtTokenUtils.hasAccess(tokenDetails, cacheName.toUpperCase() + "_ACCESS")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Retrieve data from the cache
            Map<Object, Object> cacheData = cacheManager.get(cacheName);

            return ResponseEntity.ok((Map<String, Object>) (Map<?, ?>) cacheData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}