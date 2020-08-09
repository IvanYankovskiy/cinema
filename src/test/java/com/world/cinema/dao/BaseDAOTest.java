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
import org.testcontainers.shaded.com.google.common.collect.Lists;
import org.testcontainers.shaded.com.google.common.collect.Sets;

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
class BaseDAOTest {

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
    void test_insertHall_usingSequence() throws IllegalAccessException {
        CinemaHall entity = new CinemaHall()
                .setName("TestHall");
        Integer resultId = baseDAO.insert(entity);

        Assertions.assertNotNull(resultId);
    }

    @Test
    void test_insertSeat_withoutSequence() throws IllegalAccessException {
        CinemaHall entity = new CinemaHall()
                .setId(789)
                .setName("TestHall");
        Integer hallId = baseDAO.insert(entity);

        Assertions.assertEquals(789, hallId);

        Seat seat = new Seat()
                .setHallId(hallId)
                .setSeatNumber(1)
                .setRow(1);
        Integer seatId = baseDAO.insert(seat);

        Assertions.assertNotNull(seatId);
    }

    @Test
    void test_insertMultiple_usingSequence() throws IllegalAccessException {
        CinemaHall entity = new CinemaHall()
                .setName("TestHall");
        Integer hallId = baseDAO.insert(entity);

        Assertions.assertNotNull(hallId);

        List<Seat> collectionToInsert = new ArrayList<>();
        for (int currentRow = 0; currentRow < 2; currentRow++) {
            for (int seat = 0; seat < 10; seat++) {
                collectionToInsert.add(new Seat()
                        .setHallId(hallId)
                        .setSeatNumber(seat)
                        .setRow(currentRow));
            }
        }
        boolean isSucceed = baseDAO.insertMultiple(collectionToInsert);
        Assertions.assertTrue(isSucceed);
    }

    @Test
    void test_SelectMultiple() throws IllegalAccessException, InstantiationException {
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
                        .setSeatNumber(seat)
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

    @Test
    void test_selectById() throws IllegalAccessException, InstantiationException {
        CinemaHall expected = new CinemaHall()
                .setName("TestHall");
        Integer resultId = baseDAO.insert(expected);
        expected.setId(resultId);

        Assertions.assertNotNull(resultId);

        //when
        CinemaHall selectedById = baseDAO.selectById(CinemaHall.class, resultId);

        //then
        Assertions.assertEquals(expected, selectedById);

    }

    @Test
    void test_selectByParameters() throws IllegalAccessException, InstantiationException {
        CinemaHall expectedHall = new CinemaHall()
                .setName("TestHall");
        Integer expectedHallId = baseDAO.insert(expectedHall);
        expectedHall.setId(expectedHallId);
        Assertions.assertNotNull(expectedHallId);

        CinemaHall otherHall = new CinemaHall()
                .setName("TestHall 2");
        Integer otherHallId = baseDAO.insert(otherHall);
        otherHall.setId(otherHallId);
        Assertions.assertNotNull(otherHallId);

        Seat expectedSeat = new Seat()
                .setSeatNumber(1)
                .setHallId(expectedHall.getId())
                .setRow(1)
                .setState("f");
        Integer expectedSeatId = baseDAO.insert(expectedSeat);
        expectedSeat.setId(expectedSeatId);
        Assertions.assertNotNull(expectedSeatId);

        Seat excludedSeat = new Seat()
                .setSeatNumber(2)
                .setHallId(expectedHall.getId())
                .setRow(1)
                .setState("r");
        Integer excludedSeatId = baseDAO.insert(excludedSeat);
        excludedSeat.setId(excludedSeatId);
        Assertions.assertNotNull(excludedSeatId);

        Seat otherSeat = new Seat()
                .setSeatNumber(1)
                .setHallId(otherHall.getId())
                .setRow(1)
                .setState("f");
        Integer otherSeatId = baseDAO.insert(otherSeat);
        otherSeat.setId(otherSeatId);
        Assertions.assertNotNull(otherSeatId);

        //when

        ConditionalFieldDetails hallIdIsEquals = new ConditionalFieldDetails();
        hallIdIsEquals.setFieldNameAsInDb("hall_id");
        hallIdIsEquals.setSign("=");
        hallIdIsEquals.setValue(expectedHallId);
        hallIdIsEquals.setClazz(Integer.class);

        ConditionalFieldDetails stateEquals = new ConditionalFieldDetails();
        stateEquals.setFieldNameAsInDb("state");
        stateEquals.setSign("=");
        stateEquals.setValue("f");
        stateEquals.setClazz(String.class);
        List<ConditionalFieldDetails> conditions = new ArrayList<>();
        conditions.add(hallIdIsEquals);
        conditions.add(stateEquals);

        //when
        List<Seat> resultSeats = baseDAO.selectByParametersConnectedByAnd(conditions, Seat.class);

        //then
        Assertions.assertNotNull(resultSeats);
        Assertions.assertEquals(1, resultSeats.size());
        Assertions.assertEquals(expectedSeat, resultSeats.get(0));
    }


    @Test
    void test_selectByIds() throws IllegalAccessException, InstantiationException {
        CinemaHall aHall = new CinemaHall()
                .setId(1)
                .setName("Hall A");
        CinemaHall bHall = new CinemaHall()
                .setId(2)
                .setName("Hall B");
        CinemaHall cHall = new CinemaHall()
                .setId(3)
                .setName("Hall C");

        List<CinemaHall> toInsert = Lists.newArrayList(aHall, bHall, cHall);
        Set<Integer> idsForSelection = Sets.newHashSet(1, 2);

        boolean isSuccessfullyInserted = baseDAO.insertMultiple(toInsert);
        Assertions.assertTrue(isSuccessfullyInserted, "Failure on preparation. Check insertMultiple method");

        //when
        List<CinemaHall> results = baseDAO.selectByIds(CinemaHall.class, new ArrayList<>(idsForSelection));
        Assertions.assertNotNull(results);
        Assertions.assertEquals(2, results.size());
        Assertions.assertTrue(results.stream().allMatch(r -> idsForSelection.contains(r.getId())));
    }

}