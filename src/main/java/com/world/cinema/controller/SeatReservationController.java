package com.world.cinema.controller;

import com.world.cinema.controller.dto.SeatDTO;
import com.world.cinema.service.SeatReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class SeatReservationController {

    private SeatReservationService seatReservationService;

    @Autowired
    public SeatReservationController(SeatReservationService seatReservationService) {
        this.seatReservationService = seatReservationService;
    }

    @PostMapping("/seats")
    public List<SeatDTO> reserveSeatsByIds(@RequestBody List<Integer> seatIdsToReserve) {
        return seatReservationService.reserveSeats(seatIdsToReserve);
    }

}
