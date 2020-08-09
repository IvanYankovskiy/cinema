package com.world.cinema.core.jdbc.exception;

public class ResultSetParsingException extends RuntimeException {

    public ResultSetParsingException(Class<?> clazz) {
        super("Error during parsing result set for class \"" + clazz.getName() + "\"");
    }
}
