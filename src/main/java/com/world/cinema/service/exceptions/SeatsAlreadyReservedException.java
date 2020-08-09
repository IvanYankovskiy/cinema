package com.world.cinema.service.exceptions;

public class SeatsAlreadyReservedException extends RuntimeException {
    public SeatsAlreadyReservedException(String message) {
        super(message);
    }
}
