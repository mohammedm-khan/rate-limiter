package com.pyfullstack.ratelimiter.spring;

import org.springframework.boot.jdbc.init.DataSourceScriptDatabaseInitializer;
import org.springframework.boot.jdbc.init.PlatformPlaceholderDatabaseDriverResolver;
import org.springframework.boot.sql.init.DatabaseInitializationMode;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class DataSourceInitializerStatic extends DataSourceScriptDatabaseInitializer {

    private static final String SCHEMA_LOCATION = "classpath:oracle-schema.sql";

    public DataSourceInitializerStatic(DataSource dataSource) {
        super(dataSource, getSettings(dataSource));
    }

    static DatabaseInitializationSettings getSettings(DataSource dataSource) {
        DatabaseInitializationSettings settings = new DatabaseInitializationSettings();

        // Set common initialization settings
        settings.setContinueOnError(false);
        settings.setSchemaLocations(resolveSchemaLocations(dataSource));
        settings.setMode(DatabaseInitializationMode.ALWAYS);

        // Set SQL script encoding and separator
        settings.setEncoding(StandardCharsets.UTF_8);
        settings.setSeparator("/");

        return settings;
    }

    static List<String> resolveSchemaLocations(DataSource dataSource) {
        PlatformPlaceholderDatabaseDriverResolver platformResolver = new PlatformPlaceholderDatabaseDriverResolver();
        return platformResolver.resolveAll(dataSource, new String[]{SCHEMA_LOCATION});
    }
}