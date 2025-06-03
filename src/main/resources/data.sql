-- src/main/resources/data.sql
INSERT INTO rate_limit_config (route_path, http_method, max_requests, ttl_in_seconds, tenant_id)
VALUES
    ('/api/resources', 'GET', 100, 60, NULL),
    ('/api/users', 'POST', 20, 60, NULL),
    ('/api/admin/*', 'GET', 50, 60, 'tenant1')
ON CONFLICT (route_path, http_method, tenant_id) DO NOTHING;