package com.world.cinema.service;

import com.world.cinema.controller.dto.CinemaHallDTO;
import com.world.cinema.controller.dto.CinemaHallFullDTO;
import com.world.cinema.controller.dto.SeatDTO;
import com.world.cinema.controller.mapper.CinemaHallMapper;
import com.world.cinema.controller.mapper.SeatMapper;
import com.world.cinema.core.jdbc.BaseDAO;
import com.world.cinema.core.jdbc.fields.ConditionalFieldDetails;
import com.world.cinema.domain.CinemaHall;
import com.world.cinema.domain.Seat;
import com.world.cinema.service.exceptions.CinemaHallNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CinemaHallViewService {

    private BaseDAO baseDAO;

    private CinemaHallMapper cinemaHallMapper;

    private SeatMapper seatMapper;

    @Autowired
    public CinemaHallViewService(BaseDAO baseDAO, CinemaHallMapper cinemaHallMapper, SeatMapper seatMapper) {
        this.baseDAO = baseDAO;
        this.cinemaHallMapper = cinemaHallMapper;
        this.seatMapper = seatMapper;
    }

    public CinemaHallFullDTO getCinemaHallDetailsById(Integer cinemaHallId) throws InstantiationException, IllegalAccessException {
        CinemaHall cinemaHall = baseDAO.selectById(CinemaHall.class, cinemaHallId);
        if (Objects.isNull(cinemaHall)) {
            throw new CinemaHallNotFound(cinemaHallId);
        }

        ConditionalFieldDetails conditionHallIdEquals = new ConditionalFieldDetails();
        conditionHallIdEquals.setClazz(Integer.class);
        conditionHallIdEquals.setFieldNameAsInDb("hall_id");
        conditionHallIdEquals.setValue(cinemaHallId);
        conditionHallIdEquals.setSign("=");
        List<ConditionalFieldDetails> queryParams = new ArrayList<>();
        queryParams.add(conditionHallIdEquals);
        List<Seat> seats = baseDAO.selectByParametersConnectedByAnd(queryParams, Seat.class);
        CinemaHallFullDTO cinemaHallFullDto = cinemaHallMapper.fromEntityToDto(cinemaHall);
        List<SeatDTO> seatDtos = seatMapper.fromEntitiesToDtos(seats);
        cinemaHallFullDto.setSeats(seatDtos);
        return cinemaHallFullDto;
    }

    public List<CinemaHallDTO> getAllCinemaHalls() throws InstantiationException, IllegalAccessException {
        List<CinemaHall> cinemaHalls = baseDAO.selectAll(CinemaHall.class);
        return cinemaHallMapper.fromEntitiesToSimpleDTOs(cinemaHalls);
    }

}
