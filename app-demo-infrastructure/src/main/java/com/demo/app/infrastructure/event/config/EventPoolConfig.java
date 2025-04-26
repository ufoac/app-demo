package com.demo.app.infrastructure.event.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * The type Pool config.
 */
@Configuration
@EnableAsync
@RequiredArgsConstructor
public class EventPoolConfig {
    private final AppConfig pool;

    /**
     * Custom thread pool executor.
     *
     * @return the executor
     */
    @Bean("event-pool")
    Executor customThreadPool() {
        var config = pool.getPool().get("event-pool");
        var factory = new ThreadFactoryBuilder()
                .setNameFormat("event-pool-%d")
                .setDaemon(true)
                .build();
        return new ThreadPoolExecutor(config.getMin(),
                config.getMax(),
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(config.getQueue()),
                factory,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
