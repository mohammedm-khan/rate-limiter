
DECLARE
    table_exists NUMBER;
BEGIN
    -- Check if table already exists
    SELECT COUNT(*) INTO table_exists
    FROM user_tables
    WHERE table_name = 'RATE_LIMIT_CONFIG';
    
    -- Create table if it doesn't exist
    IF table_exists = 0 THEN
        EXECUTE IMMEDIATE '
        CREATE TABLE rate_limit_config (
            id NUMBER(19) GENERATED ALWAYS AS IDENTITY,
            route_path VARCHAR2(255) NOT NULL,
            http_method VARCHAR2(20) NOT NULL,
            max_requests NUMBER(19) NOT NULL,
            ttl_in_seconds NUMBER(19) NOT NULL,
            tenant_id VARCHAR2(100),
            enabled NUMBER(1) DEFAULT 1,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT rate_limit_config_pkey PRIMARY KEY (id),
            CONSTRAINT rate_limit_config_route_path_http_method_tenant_id_key 
                UNIQUE (route_path, http_method, tenant_id)
        )';
        
        DBMS_OUTPUT.PUT_LINE('Table rate_limit_config created successfully.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Table rate_limit_config already exists.');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error creating table: ' || SQLERRM);
END;
/