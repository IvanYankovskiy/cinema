package com.world.cinema.core.jdbc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdFieldDetails extends FieldDetails {

    private String sequenceName;

    public IdFieldDetails(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public IdFieldDetails(Object value, Class clazz, String sequenceName) {
        super(value, clazz);
        this.sequenceName = sequenceName;
    }
}
