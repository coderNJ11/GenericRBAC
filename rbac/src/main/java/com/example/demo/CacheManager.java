package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CacheManager {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public CacheManager(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void store(String cacheName, Map<String, Object> data) {
        redisTemplate.opsForHash().putAll(cacheName, data);
    }

    public Map<Object, Object> get(String cacheName) {
        return redisTemplate.opsForHash().entries(cacheName);
    }
}