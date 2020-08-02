package com.world.cinema.config;

import com.world.cinema.core.jdbc.BaseDAO;
import com.world.cinema.core.jdbc.DataExtractor;
import com.world.cinema.core.jdbc.StatementBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;

@TestConfiguration
public class TestDaoBeansConfig {

    @Bean
    @Scope("prototype")
    public BaseDAO baseDataAccess(DataSource dataSource, DataExtractor dataExtractor, StatementBuilder stmntBuilder) {
        return new BaseDAO(dataSource, dataExtractor, stmntBuilder);
    }

}
