package com.pyfullstack.ratelimiter.service;

import com.pyfullstack.ratelimiter.entity.RateLimitConfig;
import com.pyfullstack.ratelimiter.repository.RateLimitConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RateLimitConfigService {

    private final RateLimitConfigRepository rateLimitConfigRepository;
    private final Map<String, RateLimitConfig> configCache;

    public RateLimitConfigService(RateLimitConfigRepository rateLimitConfigRepository) {
        this.rateLimitConfigRepository = rateLimitConfigRepository;
        this.configCache = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void loadConfigurations() {
        log.info("Loading rate limit configurations from database");
        List<RateLimitConfig> configs = rateLimitConfigRepository.findAllByEnabledTrue();
        configCache.clear();
        
        for (RateLimitConfig config : configs) {
            String key = buildKey(config.getRoutePath(), config.getHttpMethod(), config.getTenantId());
            configCache.put(key, config);
        }
        log.info("Loaded {} rate limit configurations", configCache.size());
    }

    public RateLimitConfig getConfig(String path, String method, String tenantId) {
        // First try to find tenant-specific config
        String tenantKey = buildKey(path, method, tenantId);
        RateLimitConfig config = configCache.get(tenantKey);
        
        if (config != null) {
            return config;
        }

        // If no tenant-specific config found, try to find global config
        String globalKey = buildKey(path, method, null);
        return configCache.get(globalKey);
    }

    private String buildKey(String path, String method, String tenantId) {
        return tenantId == null ? 
            String.format("%s:%s", path, method) :
            String.format("%s:%s:%s", path, method, tenantId);
    }

    public void refreshCache() {
        loadConfigurations();
    }
}