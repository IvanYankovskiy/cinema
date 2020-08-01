package com.world.cinema.domain;

import com.world.cinema.core.jdbc.annotations.ColumnName;
import com.world.cinema.core.jdbc.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("cinema_hall")
public class CinemaHall {

    @ColumnName("id")
    Integer id;

    @ColumnName("name")
    String name;
}
