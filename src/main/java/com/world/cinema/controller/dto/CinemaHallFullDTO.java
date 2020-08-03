package com.world.cinema.controller.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CinemaHallFullDTO {

    private Integer id;

    private String name;

    private List<SeatDTO> seats = new ArrayList<>();
}
