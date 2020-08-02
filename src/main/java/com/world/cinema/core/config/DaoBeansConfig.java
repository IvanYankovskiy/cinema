package com.world.cinema.core.config;

import com.world.cinema.core.jdbc.BaseDataAccess;
import com.world.cinema.core.jdbc.DataExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;

@Configuration
public class DaoBeansConfig {

    @Bean
    @Scope("prototype")
    public BaseDataAccess baseDao(DataSource dataSource, DataExtractor dataExtractor) {
        return new BaseDataAccess(dataSource, dataExtractor);
    }

}
