package com.world.cinema.controller.mapper;

import com.world.cinema.controller.dto.CinemaHallDTO;
import com.world.cinema.controller.dto.CinemaHallFullDTO;
import com.world.cinema.domain.CinemaHall;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CinemaHallMapper {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "entity.name")
    public abstract CinemaHallFullDTO fromEntityToDto(CinemaHall entity);

    public List<CinemaHallFullDTO> fromEntitiesToDTOs(Collection<CinemaHall> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        return entities
                .stream()
                .map(this::fromEntityToDto)
                .collect(Collectors.toList());
    }


    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "entity.name")
    public abstract CinemaHallDTO fromEntityToSimpleDto(CinemaHall entity);

    public List<CinemaHallDTO> fromEntitiesToSimpleDTOs(Collection<CinemaHall> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        return entities
                .stream()
                .map(this::fromEntityToSimpleDto)
                .collect(Collectors.toList());
    }


}
