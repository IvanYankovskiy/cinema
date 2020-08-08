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

    private Integer statementIndex;

    public FieldDetails() {
    }

    public FieldDetails(Object value, Class<?> clazz) {
        this.value = value;
        this.clazz = clazz;
    }

    public FieldDetails(String fieldName, Object value, Class<?> clazz) {
        this.fieldName = fieldName;
        this.value = value;
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDetails that = (FieldDetails) o;
        return Objects.equals(fieldName, that.fieldName) &&
                value.equals(that.value) &&
                clazz.equals(that.clazz) &&
                Objects.equals(statementIndex, that.statementIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, value, clazz, statementIndex);
    }
}
