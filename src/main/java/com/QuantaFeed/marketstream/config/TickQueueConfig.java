package com.QuantaFeed.marketstream.config;

import com.QuantaFeed.marketstream.model.Tick;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Configuration for the shared tick queue used in producer-consumer pattern.
 */
@Configuration
public class TickQueueConfig {

    @Value("${app.queue.capacity:1000}")
    private int queueCapacity;

    @Bean
    public BlockingQueue<Tick> tickQueue() {
        return new ArrayBlockingQueue<>(queueCapacity);
    }
}

