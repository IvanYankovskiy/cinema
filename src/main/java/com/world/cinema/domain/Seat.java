package com.world.cinema.domain;

import com.world.cinema.core.jdbc.annotations.ColumnName;
import com.world.cinema.core.jdbc.annotations.Id;
import com.world.cinema.core.jdbc.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat1 = (Seat) o;
        return id.equals(seat1.id) &&
                hallId.equals(seat1.hallId) &&
                row.equals(seat1.row) &&
                seat.equals(seat1.seat) &&
                state.equals(seat1.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hallId, row, seat, state);
    }
}
