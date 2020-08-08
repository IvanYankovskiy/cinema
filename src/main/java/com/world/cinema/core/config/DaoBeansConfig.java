package com.world.cinema.core.config;

import com.world.cinema.core.jdbc.BaseDAO;
import com.world.cinema.core.jdbc.DataExtractor;
import com.world.cinema.dao.SeatDAO;
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

    @Bean
    @Scope("prototype")
    public SeatDAO seatDao(BaseDAO baseDao) {
        return new SeatDAO(baseDao);
    }

}
