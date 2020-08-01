package com.world.cinema.core.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${app.datasource.configfile}")
    private String poolConfigFile;

    @Primary
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig(poolConfigFile);
        return new HikariDataSource(config);
    }


}
