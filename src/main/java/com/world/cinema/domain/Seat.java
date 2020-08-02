package com.world.cinema.domain;

import com.world.cinema.core.jdbc.annotations.ColumnName;
import com.world.cinema.core.jdbc.annotations.Id;
import com.world.cinema.core.jdbc.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@TableName("seats")
public class Seat {

    @Id(sequenceName = "seat_id_sequence")
    @ColumnName("id")
    private Integer id;

    @ColumnName("hall_id")
    private Integer hallId;

    @ColumnName("row")
    private Integer row;

    @ColumnName("seat")
    private Integer seat;

    @ColumnName("state")
    private String state = "f";
}
