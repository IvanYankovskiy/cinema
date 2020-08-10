package com.world.cinema.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

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
        container.addEnv("POSTGRES_USER", "application");
        container.addEnv("POSTGRES_PASSWORD", "application");
        container.addEnv("POSTGRES_DB", "cinema");
        return container;
    }

    public static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "jdbcUrl=" + container.getJdbcUrl(),
                    "username=" + container.getUsername(),
                    "password=" + container.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
            System.setProperty("DB_URL", container.getJdbcUrl());
            System.setProperty("DB_USERNAME", container.getUsername());
            System.setProperty("DB_PASSWORD", container.getPassword());
            container.waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(10)));
        }
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
