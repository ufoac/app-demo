package com.demo.app.interfaces.managment;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * The type App life state.
 */
@Component
@ConditionalOnProperty("management.endpoints.enabled-by-default")
public class AppHealthState implements HealthIndicator {

    @Override
    public Health health() {
        return Health.up()
                .withDetails(Map.of("everything", "ok")).build();
    }
}
