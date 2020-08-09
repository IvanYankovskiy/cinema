package com.world.cinema.controller;

import com.world.cinema.controller.dto.CinemaHallDTO;
import com.world.cinema.controller.dto.CinemaHallFullDTO;
import com.world.cinema.service.CinemaHallViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
public class CinemaHallController {

    private CinemaHallViewService cinemaHallViewService;

    @Autowired
    public CinemaHallController(CinemaHallViewService cinemaHallViewService) {
        this.cinemaHallViewService = cinemaHallViewService;
    }

    @GetMapping("hall/{id}/seats")
    public CinemaHallFullDTO getHallDetails(@PathVariable("id") @NotNull(message = "HallId can't be null or empty")
                                                        Integer hallId) throws IllegalAccessException, InstantiationException {
        return cinemaHallViewService.getCinemaHallDetailsById(hallId);
    }

    @GetMapping("/hall")
    public List<CinemaHallDTO> getAllCinemaHalls() throws IllegalAccessException, InstantiationException {
        return cinemaHallViewService.getAllCinemaHalls();
    }
}
