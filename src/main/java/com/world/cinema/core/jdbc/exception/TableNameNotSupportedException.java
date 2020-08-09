package com.world.cinema.core.jdbc.exception;


public class TableNameNotSupportedException extends RuntimeException {

    public TableNameNotSupportedException(Class<?> clazz) {
        super("Class \"" + clazz.getName() + "\" doesn't support @TableName annotation");
    }
}
