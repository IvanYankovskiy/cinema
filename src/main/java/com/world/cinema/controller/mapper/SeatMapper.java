package com.world.cinema.controller.mapper;

import com.world.cinema.controller.dto.SeatDTO;
import com.world.cinema.domain.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class SeatMapper {

    @Mappings({
            @Mapping(target = "id", source = "entity.id"),
            @Mapping(target = "row", source = "entity.row"),
            @Mapping(target = "seat", source = "entity.seat"),
            @Mapping(target = "state", source = "entity.state"),
    })
    public abstract SeatDTO fromEntityToDto(Seat entity);

    public List<SeatDTO> fromEntitiesToDtos(List<Seat> entities) {
        return entities.stream().map(this::fromEntityToDto).collect(Collectors.toList());
    }
}
