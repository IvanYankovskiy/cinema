package com.world.cinema.service.exceptions;

import java.util.Collection;

public class SeatsAlreadyReservedException extends RuntimeException {
    public SeatsAlreadyReservedException(String message) {
        super(message);
    }

    public SeatsAlreadyReservedException(Collection<Integer> reservedBeforeIds) {
        super("Seats " + reservedBeforeIds.toString() + " have been already reserved");
    }


}
