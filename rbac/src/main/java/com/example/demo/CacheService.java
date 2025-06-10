package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class CacheService {

    @Autowired
    CacheManager cacheManager; // Redis or other implementation injected

    @PostMapping("/{cacheName}")
    public ResponseEntity<String> storeData(
            @PathVariable String cacheName,
            @RequestBody Map<String, Object> data,
            @RequestHeader("Authorization") String token) {

        // Extract and validate JWT token
        Jwt tokenDetails = JwtTokenUtils.parseToken(token);

        // Check if the user has the right role for this cache
        if (!JwtTokenUtils.hasAccess(tokenDetails, cacheName.toUpperCase() + "_ACCESS")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        // Store data in specified cache
        cacheManager.store(cacheName, data);

        return ResponseEntity.ok("Data stored successfully");
    }

    @GetMapping("/{cacheName}")
    public ResponseEntity<Map<String, Object>> getData(
            @PathVariable String cacheName,
            @RequestHeader("Authorization") String token) {

        // Validate token and check roles
        Jwt tokenDetails = JwtTokenUtils.parseToken(token);
        if (!JwtTokenUtils.hasAccess(tokenDetails, cacheName.toUpperCase() + "_ACCESS")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Retrieve data from the cache
        Map<String, Object> cacheData = cacheManager.get(cacheName);

        return ResponseEntity.ok(cacheData);
    }
}