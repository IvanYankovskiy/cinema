package com.world.cinema.dao;

import com.world.cinema.TableCleaner;
import com.world.cinema.config.PostgresSharedContainer;
import com.world.cinema.config.TestDataSourceConfig;
import com.world.cinema.core.config.DaoBeansConfig;
import com.world.cinema.core.config.DatabaseMigrationsConfig;
import com.world.cinema.core.jdbc.BaseDAO;
import com.world.cinema.core.jdbc.fields.ConditionalFieldDetails;
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
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BaseDAO.class},
        properties = {
                "spring.liquibase.change-log=classpath:/db.changelogtest/testChangelogEntrypoint.xml",
        })
@TestPropertySource(locations={"classpath:application-test.properties"})
@ContextConfiguration(classes = {TestDataSourceConfig.class, DaoBeansConfig.class, DatabaseMigrationsConfig.class},
        initializers = {PostgresSharedContainer.Initializer.class})
@Testcontainers
@DisplayName("SeatDAO integration test")
class SeatDAOTest {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = PostgresSharedContainer.getInstance();

    @Autowired
    private SeatDAO seatDao;

    @Autowired
    private BaseDAO baseDAO;

    @Autowired
    private TableCleaner tableCleaner;

    @BeforeEach
    public void cleanTables() {
        tableCleaner.truncateAllTables(CinemaHall.class, Seat.class);
    }

    @Test
    public void test_reserveSeatsByIdsIfFree() throws IllegalAccessException, InstantiationException {
        CinemaHall cinemaHall = new CinemaHall()
                .setName("test hall");
        Integer hallId = baseDAO.insert(cinemaHall);
        Assertions.assertNotNull(hallId);

        List<Seat> collectionToInsert = new ArrayList<>();
        for (int currentRow = 1; currentRow < 3; currentRow++) {
            for (int seat = 1; seat < 3; seat++) {
                collectionToInsert.add(new Seat()
                        .setHallId(hallId)
                        .setSeat(seat)
                        .setRow(currentRow));
            }
        }
        boolean isSucceed = baseDAO.insertMultiple(collectionToInsert);
        Assertions.assertTrue(isSucceed);

        ConditionalFieldDetails conditionHallIdEquals = new ConditionalFieldDetails();
        conditionHallIdEquals.setClazz(Integer.class);
        conditionHallIdEquals.setTableFieldName("hall_id");
        conditionHallIdEquals.setValue(cinemaHall.getId());
        conditionHallIdEquals.setSign("=");
        List<ConditionalFieldDetails> queryParams = new ArrayList<>();
        queryParams.add(conditionHallIdEquals);
        List<Seat> seats = baseDAO.selectByParametersConnectedByAnd(queryParams, Seat.class);
        Assertions.assertNotNull(seats);

        List<Integer> toUpdate = seats.stream()
                .filter(s -> {
                    return s.getRow().equals(1)
                            && (s.getSeat().equals(1) || s.getSeat().equals(1)
                            && (s.getState().equals("f")));
                })
                .map(Seat::getId)
                .collect(Collectors.toList());

        List<Integer> orderedResults = seatDao.reserveSeatsByIdsIfFree(toUpdate);
        Assertions.assertTrue(orderedResults.stream().allMatch(r -> r == 1));
    }




}