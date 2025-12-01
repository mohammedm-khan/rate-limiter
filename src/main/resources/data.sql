-- src/main/resources/data.sql
INSERT INTO rate_limit_config (route_path, http_method, max_requests, ttl_in_seconds, tenant_id)
VALUES
    ('/api/v1/process1', 'GET', 10, 60, '123'),
        ('/api/v1/process2', 'GET', 5, 30, '123'),
    ('/api/v1/process3', 'GET', 10, 90, '123')
ON CONFLICT (route_path, http_method, tenant_id) DO NOTHING;