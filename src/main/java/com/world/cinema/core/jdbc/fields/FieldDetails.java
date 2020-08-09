package com.world.cinema.core.jdbc.fields;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class FieldDetails {

    private String fieldNameAsInDb;

    private Object value;

    private Class<?> clazz;

    private Integer statementIndex;

    public FieldDetails() {
    }

    public FieldDetails(Object value, Class<?> clazz) {
        this.value = value;
        this.clazz = clazz;
    }

    public FieldDetails(String fieldNameAsInDb, Object value, Class<?> clazz) {
        this.fieldNameAsInDb = fieldNameAsInDb;
        this.value = value;
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDetails that = (FieldDetails) o;
        return fieldNameAsInDb.equals(that.fieldNameAsInDb) &&
                value.equals(that.value) &&
                clazz.equals(that.clazz) &&
                Objects.equals(statementIndex, that.statementIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldNameAsInDb, value, clazz, statementIndex);
    }
}
