package com.world.cinema.service.exceptions;

public class CinemaHallNotFound extends RuntimeException {
    public CinemaHallNotFound(Integer cinemaHallId) {
        super("Cinema hall with provided id: " + cinemaHallId + " is not found");
    }
}
