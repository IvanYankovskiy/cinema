package com.world.cinema.util;

import com.world.cinema.service.exceptions.ParameterCannotBeNullOrEmpty;

import java.util.Objects;

public class CheckUtils {

    private CheckUtils() {
    }

    public static <T> void ifNullThenThrowException(T parameter, String parameterName) {
        if (Objects.isNull(parameter)) {
            throw new ParameterCannotBeNullOrEmpty(parameterName);
        }
    }
}
