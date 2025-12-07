package com.QuantaFeed.marketstream.config;

import com.QuantaFeed.marketstream.model.Tick;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    // Provide a shared Jackson ObjectMapper for WebSocket client and others
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
