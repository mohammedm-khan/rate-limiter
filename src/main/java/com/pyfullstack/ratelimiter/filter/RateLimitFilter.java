package com.pyfullstack.ratelimiter.filter;

import com.pyfullstack.ratelimiter.entity.RateLimitConfig;
import com.pyfullstack.ratelimiter.service.RateLimitConfigService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, String> redisTemplate;
    private final RateLimitConfigService configService;

    public RateLimitFilter(RedisTemplate<String, String> redisTemplate,
                           RateLimitConfigService configService) {
        this.redisTemplate = redisTemplate;
        this.configService = configService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tenantId = request.getHeader("tenant_id");
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Get rate limit config for this route
        RateLimitConfig config = configService.getConfig(path, method, tenantId);

        if (config == null) {
            // No rate limit config found, allow the request
            filterChain.doFilter(request, response);
            return;
        }

        String redisKey = buildRedisKey(tenantId, method, path);

        Long requestCount = redisTemplate.opsForValue().increment(redisKey);

        if (requestCount == 1) {
            redisTemplate.expire(redisKey, config.getTtlInSeconds(), TimeUnit.SECONDS);
        }

        if (requestCount > config.getMaxRequests()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded. Try again later.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String buildRedisKey(String tenantId, String method, String path) {
        return String.join(":", "rate_limit",
                tenantId != null ? tenantId : "global",
                method,
                path
        );
    }
}