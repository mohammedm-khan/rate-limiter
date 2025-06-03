CREATE TABLE IF NOT EXISTS rate_limit_config (
    id BIGSERIAL PRIMARY KEY,
    route_path VARCHAR(255) NOT NULL,
    http_method VARCHAR(20) NOT NULL,
    max_requests BIGINT NOT NULL,
    ttl_in_seconds BIGINT NOT NULL,
    tenant_id VARCHAR(100),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (route_path, http_method, tenant_id)
);