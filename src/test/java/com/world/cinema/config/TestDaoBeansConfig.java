package com.world.cinema.config;

import com.world.cinema.core.jdbc.BaseDataAccess;
import com.world.cinema.core.jdbc.DataExtractor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;

@TestConfiguration
public class TestDaoBeansConfig {

    @Bean
    @Scope("prototype")
    public BaseDataAccess baseDataAccess(DataSource dataSource, DataExtractor dataExtractor) {
        return new BaseDataAccess(dataSource, dataExtractor);
    }

}
