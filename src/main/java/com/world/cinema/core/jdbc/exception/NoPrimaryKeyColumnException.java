package com.world.cinema.core.jdbc.exception;

public class NoPrimaryKeyColumnException extends RuntimeException {
    public NoPrimaryKeyColumnException(Class<?> clazz) {
        super("Class \"" + clazz + "\" has no of one annotion: @Id, @ColumnNmae or both");
    }
}
