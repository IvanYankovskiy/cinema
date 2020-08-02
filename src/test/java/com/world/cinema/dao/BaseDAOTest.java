package com.world.cinema.dao;

import com.world.cinema.TableCleaner;
import com.world.cinema.config.PostgresSharedContainer;
import com.world.cinema.config.TestDataSourceConfig;
import com.world.cinema.core.config.DaoBeansConfig;
import com.world.cinema.core.config.DatabaseMigrationsConfig;
import com.world.cinema.core.jdbc.BaseDAO;
import com.world.cinema.domain.CinemaHall;
import com.world.cinema.domain.Seat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BaseDAO.class},
        properties = {
                "spring.liquibase.change-log=classpath:/db.changelogtest/testChangelogEntrypoint.xml",
        })
@TestPropertySource(locations={"classpath:application-test.properties"})
@ContextConfiguration(classes = {TestDataSourceConfig.class, DaoBeansConfig.class, DatabaseMigrationsConfig.class},
        initializers = {PostgresSharedContainer.Initializer.class})
@Testcontainers
@DisplayName("BaseDAO integration test")
public class BaseDAOTest {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = PostgresSharedContainer.getInstance();

    @Autowired
    BaseDAO baseDAO;

    @Autowired
    TableCleaner tableCleaner;

    @BeforeEach
    public void cleanTables() {
        tableCleaner.truncateAllTables(CinemaHall.class, Seat.class);
    }

    @Test
    public void test_insertHall_usingSequence() throws IllegalAccessException {
        CinemaHall entity = new CinemaHall()
                .setName("TestHall");
        Integer resultId = baseDAO.insert(entity);

        Assertions.assertNotNull(resultId);
    }

    @Test
    public void test_insertSeat_withoutSequence() throws IllegalAccessException {
        CinemaHall entity = new CinemaHall()
                .setId(789)
                .setName("TestHall");
        Integer hallId = baseDAO.insert(entity);

        Assertions.assertEquals(789, hallId);

        Seat seat = new Seat()
                .setHallId(hallId)
                .setSeat(1)
                .setRow(1);
        Integer seatId = baseDAO.insert(seat);

        Assertions.assertNotNull(seatId);
    }

    @Test
    public void test_insertMultiple_usingSequence() throws IllegalAccessException {
        CinemaHall entity = new CinemaHall()
                .setName("TestHall");
        Integer hallId = baseDAO.insert(entity);

        Assertions.assertNotNull(hallId);

        List<Seat> collectionToInsert = new ArrayList<>();
        for (int currentRow = 0; currentRow < 2; currentRow++) {
            for (int seat = 0; seat < 10; seat++) {
                collectionToInsert.add(new Seat()
                        .setHallId(hallId)
                        .setSeat(seat)
                        .setRow(currentRow));
            }
        }
        boolean isSucceed = baseDAO.insertMultiple(collectionToInsert);
        Assertions.assertTrue(isSucceed);
    }

    @Test
    public void test_SelectMultiple() throws IllegalAccessException, InstantiationException {
        CinemaHall entity = new CinemaHall()
                .setName("TestHall");
        Integer hallId = baseDAO.insert(entity);

        Assertions.assertNotNull(hallId);

        Set<Seat> expectedSeats = new HashSet<>();
        for (int currentRow = 1; currentRow < 3; currentRow++) {
            for (int seat = 1; seat < 11; seat++) {
                Integer seatId = currentRow * 1000 + seat;
                expectedSeats.add(new Seat()
                        .setId(seatId)
                        .setHallId(hallId)
                        .setSeat(seat)
                        .setRow(currentRow));
            }
        }
        boolean isSucceed = baseDAO.insertMultiple(new ArrayList<>(expectedSeats));
        Assertions.assertTrue(isSucceed);

        //when
        List<Seat> seats = baseDAO.selectAll(Seat.class);

        Assertions.assertNotNull(seats);
        Assertions.assertEquals(expectedSeats, new HashSet<>(seats));
    }

}