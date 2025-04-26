package com.demo.app.infrastructure.event.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Map;


/**
 * The type App config.
 */
@ConfigurationProperties("app")
@Validated
@Data
public class AppConfig {
    private Map<String, ThreadPollConfig> pool;
    private Map<String, Feature> feature;

    /**
     * The type Config.
     */
    @Data
    public static class ThreadPollConfig {
        private Integer min;
        private Integer max;
        private Integer queue;
    }

    /**
     * The type Feature.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Feature {
        private Boolean enabled;
        private String desc;
    }
}
