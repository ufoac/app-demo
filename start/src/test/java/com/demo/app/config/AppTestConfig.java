package com.demo.app.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The type Test config.
 */
@Configuration
@EnableCaching
@EnableAsync
@ConfigurationPropertiesScan
@EnableRetry
public class AppTestConfig {
}