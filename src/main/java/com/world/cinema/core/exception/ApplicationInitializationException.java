package com.world.cinema.core.exception;

public class ApplicationInitializationException extends RuntimeException {

    public ApplicationInitializationException() {
        super("Error on start of application: population database values was failed");
    }
}
