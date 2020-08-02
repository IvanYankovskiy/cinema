package com.world.cinema.config;

import com.world.cinema.core.jdbc.DataExtractor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.util.Objects;

@TestConfiguration
public class TestDataSourceConfig {

    public static PostgreSQLContainer postgreSQLContainer = PostgresSharedContainer.getInstance();

    @Value("${app.datasource.configfile}")
    private String poolConfigFile;

    @Bean
    public PostgreSQLContainer postgreSQLContainer() {
        return PostgresSharedContainer.getInstance();
    }

    @Primary
    @Bean
    public DataSource dataSource(PostgreSQLContainer postgreSQLContainer) {
        if (Objects.isNull(poolConfigFile)) {
            throw new RuntimeException("NO TEST PROPERTIES FILE!!!");
        }
        HikariConfig config = new HikariConfig(poolConfigFile);
        config.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        config.setUsername(postgreSQLContainer.getUsername());
        config.setPassword(postgreSQLContainer.getPassword());
        return new HikariDataSource(config);
    }

    @Bean
    public DataExtractor dataExtractor() {
        return new DataExtractor();
    }

}
