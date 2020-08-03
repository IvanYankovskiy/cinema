package com.world.cinema.core.jdbc.fields;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class FieldDetails {

    private String fieldName;

    private Object value;

    private Class<?> clazz;

    public FieldDetails() {
    }

    public FieldDetails(Object value, Class<?> clazz) {
        this.value = value;
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDetails that = (FieldDetails) o;
        return value.equals(that.value) &&
                clazz.equals(that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, clazz);
    }
}
