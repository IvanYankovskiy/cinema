package com.world.cinema.core.jdbc;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.Objects;

@Getter
@Setter
public class FieldDetails {

    private Object value;

    private Type type;

    public FieldDetails() {
    }

    public FieldDetails(Object value, Type type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDetails that = (FieldDetails) o;
        return value.equals(that.value) &&
                type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }
}
