package com.world.cinema.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CinemaHallNotFoundException extends RuntimeException {
    public CinemaHallNotFoundException(Integer cinemaHallId) {
        super("Cinema hall with provided id: " + cinemaHallId + " is not found");
    }
}
