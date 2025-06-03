package com.pyfullstack.ratelimiter.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

@Slf4j
@Component
public class DatabaseInitializationListener {

    public void initializeDatabase(DataSource dataSource, ResourceDatabasePopulator populator) {
        try {
            log.info("Starting database initialization...");
            DatabasePopulatorUtils.execute(populator, dataSource);
            log.info("Database initialization completed successfully");
        } catch (Exception e) {
            log.error("Error during database initialization", e);
            throw e;
        }
    }
}