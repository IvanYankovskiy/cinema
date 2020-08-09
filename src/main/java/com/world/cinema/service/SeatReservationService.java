package com.world.cinema.service;

import com.world.cinema.controller.dto.SeatDTO;
import com.world.cinema.controller.mapper.SeatMapper;
import com.world.cinema.dao.SeatDAO;
import com.world.cinema.domain.Seat;
import com.world.cinema.service.exceptions.SeatsAlreadyReservedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeatReservationService {

    private SeatDAO seatDao;

    private SeatMapper seatMapper;

    @Autowired
    public SeatReservationService(SeatDAO seatDao, SeatMapper seatMapper) {
        this.seatDao = seatDao;
        this.seatMapper = seatMapper;
    }

    public List<SeatDTO> reserveSeats(@Validated @NotEmpty(message = "Ids can't be empty") List<Integer> seatIds) {
        int numOfReservedSeats = seatDao.reserveSeatsByIdsIfAllAreFree(seatIds);
        List<Seat> desiredSeats = seatDao.selectByIds(seatIds);
        if (numOfReservedSeats != seatIds.size()) {
            Set<Integer> reservedBeforeIds = desiredSeats.stream()
                    .filter(ds -> "r".equalsIgnoreCase(ds.getState()))
                    .map(Seat::getId)
                    .collect(Collectors.toSet());
            throw new SeatsAlreadyReservedException(reservedBeforeIds);
        }
        return seatMapper.fromEntitiesToDtos(desiredSeats);
    }
}
