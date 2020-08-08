package com.world.cinema.config;

import com.world.cinema.core.jdbc.BaseDAO;
import com.world.cinema.core.jdbc.DataExtractor;
import com.world.cinema.dao.SeatDAO;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;

@TestConfiguration
public class TestDaoBeansConfig {

    @Bean
    @Scope("prototype")
    public BaseDAO baseDataAccess(DataSource dataSource, DataExtractor dataExtractor) {
        return new BaseDAO(dataSource, dataExtractor);
    }

    @Bean
    @Scope("prototype")
    public SeatDAO seatDao(BaseDAO baseDataAccess) {
        return new SeatDAO(baseDataAccess);
    }

}
