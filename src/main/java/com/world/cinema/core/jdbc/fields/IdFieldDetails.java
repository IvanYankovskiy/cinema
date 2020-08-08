package com.world.cinema.core.jdbc.fields;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class IdFieldDetails extends FieldDetails {

    private String sequenceName;

    public IdFieldDetails(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public IdFieldDetails(Object value, Class<?> clazz, String sequenceName) {
        super(value, clazz);
        this.sequenceName = sequenceName;
    }

    public IdFieldDetails(String fieldName, Object value, Class<?> clazz, String sequenceName) {
        super(fieldName, value, clazz);
        this.sequenceName = sequenceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IdFieldDetails that = (IdFieldDetails) o;
        return sequenceName.equals(that.sequenceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sequenceName);
    }
}
