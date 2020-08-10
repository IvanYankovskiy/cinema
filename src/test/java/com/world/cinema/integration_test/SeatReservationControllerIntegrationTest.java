package com.world.cinema.integration_test;

import com.world.cinema.CinemaApplication;
import com.world.cinema.TableCleaner;
import com.world.cinema.config.PostgresSharedContainer;
import com.world.cinema.core.jdbc.BaseDAO;
import com.world.cinema.domain.CinemaHall;
import com.world.cinema.domain.Seat;
import com.world.cinema.service.exceptions.SeatsAlreadyReservedException;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {CinemaApplication.class},
        properties = {
                "spring.liquibase.change-log=classpath:/db.changelogtest/testChangelogEntrypoint.xml",
        })
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:application-test.properties"})
@ContextConfiguration(
        initializers = {PostgresSharedContainer.Initializer.class})
@Testcontainers
@DisplayName("SeatReservationController integration test")
class SeatReservationControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = PostgresSharedContainer.getInstance();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TableCleaner tableCleaner;

    @Autowired
    private BaseDAO baseDataAccess;

    @BeforeEach
    public void cleanTables() {
        tableCleaner.truncateAllTables(CinemaHall.class, Seat.class);
    }

    @Test
    void test_reserveSeatsByIds_whenFree_thenOk() throws Exception {
        CinemaHall firstHall = new CinemaHall()
                .setId(1)
                .setName("First hall");
        List<CinemaHall> halls = Lists.newArrayList(firstHall);
        boolean isInsertionSucceed = baseDataAccess.insertMultiple(halls);
        Assertions.assertTrue(isInsertionSucceed, "Data preparation was failed: Cinema halls");

        Seat seat1 = new Seat()
                .setId(1)
                .setHallId(firstHall.getId())
                .setRow(1)
                .setSeatNumber(1);
        Seat seat2 = new Seat()
                .setId(2)
                .setHallId(firstHall.getId())
                .setRow(1)
                .setSeatNumber(2);
        ArrayList<Seat> seats = Lists.newArrayList(seat1, seat2);
        boolean insertMultipleSeats = baseDataAccess.insertMultiple(seats);
        Assertions.assertTrue(insertMultipleSeats, "Data preparation was failed: seats for cinema hall");
        List<Integer> idsToReserve = seats.stream().map(Seat::getId).collect(Collectors.toList());

        mvc.perform(MockMvcRequestBuilders.post("/seats",firstHall.getId())
                .content(idsToReserve.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(seat1.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].row", Matchers.is(seat1.getRow())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].seat", Matchers.is(seat1.getSeatNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].state", Matchers.is("r")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(seat2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].row", Matchers.is(seat2.getRow())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].seat", Matchers.is(seat2.getSeatNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].state", Matchers.is("r")));

    }

    @Test
    void test_reserveSeatsByIds_whenSomeSeatWasReservedBefore_thenException() throws Exception {
        CinemaHall firstHall = new CinemaHall()
                .setId(1)
                .setName("First hall");
        CinemaHall secondHall = new CinemaHall()
                .setId(2)
                .setName("Second hall");
        List<CinemaHall> halls = Lists.newArrayList(firstHall, secondHall);
        boolean isInsertionSucceed = baseDataAccess.insertMultiple(halls);
        Assertions.assertTrue(isInsertionSucceed, "Data preparation was failed: Cinema halls");

        Seat seat1 = new Seat()
                .setId(1)
                .setHallId(firstHall.getId())
                .setRow(1)
                .setSeatNumber(1);
        Seat seat2 = new Seat()
                .setId(2)
                .setHallId(firstHall.getId())
                .setRow(1)
                .setSeatNumber(2)
                .setState("r");
        ArrayList<Seat> seats = Lists.newArrayList(seat1, seat2);
        boolean insertMultipleSeats = baseDataAccess.insertMultiple(seats);
        Assertions.assertTrue(insertMultipleSeats, "Data preparation was failed: seats for cinema hall");
        List<Integer> idsToReserve = seats.stream().map(Seat::getId).collect(Collectors.toList());

        mvc.perform(MockMvcRequestBuilders.post("/seats")
                .content(idsToReserve.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof SeatsAlreadyReservedException))
                .andExpect(result ->
                        Assertions.assertEquals("Seats [" + seat2.getId() + "] have been already reserved", result.getResolvedException().getMessage()));

    }

}
