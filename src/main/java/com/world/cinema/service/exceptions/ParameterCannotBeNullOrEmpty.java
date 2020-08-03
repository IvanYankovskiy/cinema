package com.world.cinema.service.exceptions;

public class ParameterCannotBeNullOrEmpty extends RuntimeException {
    public ParameterCannotBeNullOrEmpty(String parameterName) {
        super("Parameter \"" + parameterName +"\" can't be null or empty");
    }
}
