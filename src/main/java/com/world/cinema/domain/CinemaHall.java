package com.world.cinema.domain;

import com.world.cinema.core.jdbc.annotations.ColumnName;
import com.world.cinema.core.jdbc.annotations.Id;
import com.world.cinema.core.jdbc.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName("cinema_hall")
public class CinemaHall {

    @Id(sequenceName = "hall_id_sequence")
    @ColumnName("id")
    Integer id;

    @ColumnName("name")
    String name;
}
