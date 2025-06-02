package com.pyfullstack.ratelimiter.repository;

import com.pyfullstack.ratelimiter.entity.RateLimitConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RateLimitConfigRepository extends JpaRepository<RateLimitConfig, Long> {

    // Find all enabled configurations
    List<RateLimitConfig> findAllByEnabledTrue();

    // Find specific config for tenant
    Optional<RateLimitConfig> findByRoutePathAndHttpMethodAndTenantIdAndEnabledTrue(
            String routePath,
            String httpMethod,
            String tenantId
    );

    // Find global config (no tenant)
    Optional<RateLimitConfig> findByRoutePathAndHttpMethodAndTenantIdIsNullAndEnabledTrue(
            String routePath,
            String httpMethod
    );

    // Find all configs for a specific tenant
    List<RateLimitConfig> findAllByTenantIdAndEnabledTrue(String tenantId);

    // Find all configs for a specific route path
    List<RateLimitConfig> findAllByRoutePathAndEnabledTrue(String routePath);

    // Find all configs for a specific HTTP method
    List<RateLimitConfig> findAllByHttpMethodAndEnabledTrue(String httpMethod);

    // Check if config exists
    boolean existsByRoutePathAndHttpMethodAndTenantId(
            String routePath,
            String httpMethod,
            String tenantId
    );

    // Custom query to find configs with route path pattern matching
    @Query("SELECT r FROM RateLimitConfig r WHERE r.routePath LIKE :pathPattern AND r.enabled = true")
    List<RateLimitConfig> findByRoutePathPattern(@Param("pathPattern") String pathPattern);

    // Delete configs by tenant ID
    void deleteByTenantId(String tenantId);

    // Soft delete (disable) by ID
    @Query("UPDATE RateLimitConfig r SET r.enabled = false WHERE r.id = :id")
    void softDeleteById(@Param("id") Long id);

    // Count active configs per tenant
    @Query("SELECT COUNT(r) FROM RateLimitConfig r WHERE r.tenantId = :tenantId AND r.enabled = true")
    long countActiveConfigsByTenant(@Param("tenantId") String tenantId);

    // Find configs with rate limit above threshold
    @Query("SELECT r FROM RateLimitConfig r WHERE r.maxRequests > :threshold AND r.enabled = true")
    List<RateLimitConfig> findConfigsWithHighRateLimit(@Param("threshold") Long threshold);

    // Find configs by multiple tenant IDs
    List<RateLimitConfig> findByTenantIdInAndEnabledTrue(List<String> tenantIds);

    // Find configs with expiration time less than specified seconds
    List<RateLimitConfig> findByTtlInSecondsLessThanAndEnabledTrue(Long seconds);
}