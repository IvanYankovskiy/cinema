package com.world.cinema.core.jdbc.fields;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class FieldDetails {

    private String tableFieldName;

    private Object value;

    private Class<?> clazz;

    private Integer statementIndex;

    public FieldDetails() {
    }

    public FieldDetails(Object value, Class<?> clazz) {
        this.value = value;
        this.clazz = clazz;
    }

    public FieldDetails(String tableFieldName, Object value, Class<?> clazz) {
        this.tableFieldName = tableFieldName;
        this.value = value;
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDetails that = (FieldDetails) o;
        return tableFieldName.equals(that.tableFieldName) &&
                value.equals(that.value) &&
                clazz.equals(that.clazz) &&
                Objects.equals(statementIndex, that.statementIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableFieldName, value, clazz, statementIndex);
    }
}
