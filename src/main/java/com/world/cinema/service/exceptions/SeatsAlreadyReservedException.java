package com.world.cinema.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collection;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SeatsAlreadyReservedException extends RuntimeException {

    public SeatsAlreadyReservedException(Collection<Integer> reservedBeforeIds) {
        super("Seats " + reservedBeforeIds.toString() + " have been already reserved");
    }


}
