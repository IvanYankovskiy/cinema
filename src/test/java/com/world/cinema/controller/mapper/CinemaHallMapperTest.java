package com.world.cinema.controller.mapper;

import com.world.cinema.controller.dto.CinemaHallFullDTO;
import com.world.cinema.domain.CinemaHall;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CinemaHallMapperTest {

    private CinemaHallMapper mapper = Mappers.getMapper(CinemaHallMapper.class);

    @Test
    void testFromEntityToDto() {

        CinemaHall cinemaHall = new CinemaHall()
                .setId(1)
                .setName("GreenHall");

        CinemaHallFullDTO resultDto = mapper.fromEntityToDto(cinemaHall);

        CinemaHallFullDTO expectedDto = new CinemaHallFullDTO();
        expectedDto.setId(1);
        expectedDto.setName("GreenHall");
        assertEquals(expectedDto.getId(), resultDto.getId());
        assertEquals(expectedDto.getName(), resultDto.getName());
    }

}