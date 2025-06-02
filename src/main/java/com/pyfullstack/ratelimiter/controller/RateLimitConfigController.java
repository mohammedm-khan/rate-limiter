package com.pyfullstack.ratelimiter.controller;

import com.pyfullstack.ratelimiter.entity.RateLimitConfig;
import com.pyfullstack.ratelimiter.repository.RateLimitConfigRepository;
import com.pyfullstack.ratelimiter.service.RateLimitConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rate-limits")
@RequiredArgsConstructor
public class RateLimitConfigController {

    private final RateLimitConfigRepository rateLimitConfigRepository;
    private final RateLimitConfigService rateLimitConfigService;

    @PostMapping
    public ResponseEntity<RateLimitConfig> createConfig(@RequestBody RateLimitConfig config) {
        RateLimitConfig savedConfig = rateLimitConfigRepository.save(config);
        rateLimitConfigService.refreshCache();
        return ResponseEntity.ok(savedConfig);
    }

    @GetMapping
    public ResponseEntity<List<RateLimitConfig>> getAllConfigs() {
        return ResponseEntity.ok(rateLimitConfigRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RateLimitConfig> updateConfig(
            @PathVariable Long id,
            @RequestBody RateLimitConfig config) {
        if (!rateLimitConfigRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        config.setId(id);
        RateLimitConfig updatedConfig = rateLimitConfigRepository.save(config);
        rateLimitConfigService.refreshCache();
        return ResponseEntity.ok(updatedConfig);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfig(@PathVariable Long id) {
        if (!rateLimitConfigRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        rateLimitConfigRepository.deleteById(id);
        rateLimitConfigService.refreshCache();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-cache")
    public ResponseEntity<Void> refreshCache() {
        rateLimitConfigService.refreshCache();
        return ResponseEntity.ok().build();
    }
}