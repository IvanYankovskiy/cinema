package com.world.cinema.core.config;

import com.world.cinema.core.jdbc.BaseDAO;
import com.world.cinema.core.jdbc.DataExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;

@Configuration
public class DaoBeansConfig {

    @Bean
    @Scope("prototype")
    public BaseDAO baseDao(DataSource dataSource, DataExtractor dataExtractor) {
        return new BaseDAO(dataSource, dataExtractor);
    }

}
