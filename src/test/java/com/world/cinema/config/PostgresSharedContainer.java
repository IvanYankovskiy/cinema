package com.world.cinema.config;

import org.springframework.stereotype.Component;
import org.testcontainers.containers.PostgreSQLContainer;

@Component
public class PostgresSharedContainer extends PostgreSQLContainer<PostgresSharedContainer> {

    private static final String IMAGE_VERSION = "postgres:9.6";

    private static PostgresSharedContainer container;

    public PostgresSharedContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresSharedContainer getInstance() {
        if (container == null) {
            container = new PostgresSharedContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
