package com.world.cinema.core.jdbc;

import com.world.cinema.domain.CinemaHall;
import com.world.cinema.domain.Seat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Use for data creation
 */
@Slf4j
@Component
public class DataInitializer {

    private BaseDAO baseDAO;

    @Autowired
    public DataInitializer(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) throws IllegalAccessException, InstantiationException {
        List<Seat> seats = baseDAO.selectAll(Seat.class);
        if (!seats.isEmpty()) {
            return;
        }
        List<CinemaHall> cinemaHalls = createCinemaHalls();
        createSeatsForEachHall(cinemaHalls);
    }

    private List<CinemaHall> createCinemaHalls() throws IllegalAccessException {
        List<CinemaHall> cinemaHalls = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            cinemaHalls.add(new CinemaHall()
                    .setId(i)
                    .setName("Hall " + i));
        }
        baseDAO.insertMultiple(cinemaHalls);
        return cinemaHalls;
    }

    private void createSeatsForEachHall(List<CinemaHall> cinemaHalls) throws IllegalAccessException {
        List<Seat> seats = new ArrayList<>();
        for (CinemaHall hall : cinemaHalls) {
            for (int row = 1; row <= 10; row++) {
                for (int seat = 1; seat <= 20; seat++) {
                    String[] possibleStates = {"f", "r"};
                    int possibleStateIndex = new Random().nextInt(possibleStates.length);
                    seats.add(new Seat()
                    .setHallId(hall.getId())
                    .setState(possibleStates[possibleStateIndex])
                    .setRow(row)
                    .setSeat(seat));
                }
            }
        }
        boolean isSuccess = baseDAO.insertMultiple(seats);
        if (!isSuccess) {
            throw new RuntimeException("Faild to fill database with initial data");
        }
    }

}
