package com.world.cinema.controller.mapper;

import com.world.cinema.controller.dto.SeatDTO;
import com.world.cinema.domain.Seat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class SeatMapperTest {

    private SeatMapper mapper = Mappers.getMapper(SeatMapper.class);

    @Test
    void testFromEntityToDto() {
        Seat entity = new Seat()
                .setId(1)
                .setRow(1)
                .setHallId(1)
                .setSeatNumber(1)
                .setState("f");

        SeatDTO resultDto = mapper.fromEntityToDto(entity);

        SeatDTO expectedDto = new SeatDTO();
        expectedDto.setId(1);
        expectedDto.setRow(1);
        expectedDto.setSeat(1);
        expectedDto.setState("f");

        Assertions.assertEquals(expectedDto.getId(), resultDto.getId());
        Assertions.assertEquals(expectedDto.getRow(), resultDto.getRow());
        Assertions.assertEquals(expectedDto.getSeat(), resultDto.getSeat());
        Assertions.assertEquals(expectedDto.getState(), resultDto.getState());
    }

}