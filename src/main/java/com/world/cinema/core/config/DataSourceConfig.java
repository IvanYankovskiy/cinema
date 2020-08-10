package com.world.cinema.core.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
public class DataSourceConfig {

    @Value("${app.datasource.configfile}")
    private String poolConfigFile;

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig(poolConfigFile);
        /* dirty hack to make it work (tests, app), because on integration tests I can't adjust datasource properly*/
        if (Objects.nonNull(env.getProperty("jdbcUrl"))) {
            config.setJdbcUrl(env.getProperty("jdbcUrl"));
            config.setUsername(env.getProperty("username"));
            config.setPassword(env .getProperty("password"));
        }
        return new HikariDataSource(config);
    }
}
