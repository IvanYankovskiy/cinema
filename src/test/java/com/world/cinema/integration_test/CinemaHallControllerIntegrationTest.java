package com.world.cinema.integration_test;

import com.world.cinema.CinemaApplication;
import com.world.cinema.TableCleaner;
import com.world.cinema.config.PostgresSharedContainer;
import com.world.cinema.config.TestDataSourceConfig;
import com.world.cinema.core.jdbc.BaseDAO;
import com.world.cinema.domain.CinemaHall;
import com.world.cinema.domain.Seat;
import com.world.cinema.service.exceptions.CinemaHallNotFoundException;
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

import java.util.List;

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
        classes = {TestDataSourceConfig.class},
        initializers = {PostgresSharedContainer.Initializer.class})
@Testcontainers
@DisplayName("CinemaHallController integration test")
class CinemaHallControllerIntegrationTest {

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
    void test_getAllCinemaHalls() throws Exception {
        CinemaHall firstHall = new CinemaHall()
                .setId(1)
                .setName("First hall");
        CinemaHall secondHall = new CinemaHall()
                .setId(2)
                .setName("Second hall");
        List<CinemaHall> halls = Lists.newArrayList(firstHall, secondHall);
        boolean isInsertionSucceed = baseDataAccess.insertMultiple(halls);
        Assertions.assertTrue(isInsertionSucceed, "Data preparation was failed");

        //when
        mvc.perform(MockMvcRequestBuilders.get("/hall")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(firstHall.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is(firstHall.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(secondHall.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is(secondHall.getName())));
    }

    @Test
    void test_getHallDetails() throws Exception {
        CinemaHall firstHall = new CinemaHall()
                .setId(1)
                .setName("First hall");
        CinemaHall secondHall = new CinemaHall()
                .setId(2)
                .setName("Second hall");
        List<CinemaHall> halls = Lists.newArrayList(firstHall, secondHall);
        boolean isInsertionSucceed = baseDataAccess.insertMultiple(halls);
        Assertions.assertTrue(isInsertionSucceed, "Data preparation was failed: Cinema halls");

        Seat seat = new Seat()
                .setId(1)
                .setHallId(firstHall.getId())
                .setRow(1)
                .setSeatNumber(1);
        Integer seatId = baseDataAccess.insert(seat);
        Assertions.assertNotNull(seatId, "Data preparation was failed: seat for cinema hall");
        Assertions.assertEquals(seat.getId(), seatId);

        //when
        mvc.perform(MockMvcRequestBuilders.get("/hall/{id}/seats",firstHall.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(firstHall.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(firstHall.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seats.[0].id", Matchers.is(seat.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seats.[0].row", Matchers.is(seat.getRow())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seats.[0].seat", Matchers.is(seat.getSeatNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seats.[0].state", Matchers.is(seat.getState())));
    }

    @Test
    void test_getHallDetails_whenHallIdNotFound_thenException() throws Exception {
        //when
        int hallId = 4658;
        mvc.perform(MockMvcRequestBuilders.get("/hall/{id}/seats", hallId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CinemaHallNotFoundException))
                .andExpect(result ->
                        Assertions.assertEquals("Cinema hall with provided id: " + hallId + " is not found", result.getResolvedException().getMessage()));
    }
}
