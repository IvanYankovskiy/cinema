package com.world.cinema.core.jdbc.exception;

public class QueryParametersAreEmptyException extends RuntimeException {
    public QueryParametersAreEmptyException(String message) {
        super("Provided query parameters \"" + message + "\" can't be empty!");
    }
}
