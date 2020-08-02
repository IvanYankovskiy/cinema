package com.world.cinema.core.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataInitializer {



    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {

    }

}
