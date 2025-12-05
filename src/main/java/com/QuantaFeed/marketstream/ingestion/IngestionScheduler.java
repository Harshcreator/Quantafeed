package com.QuantaFeed.marketstream.ingestion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Scheduler that coordinates tick production and consumption.
 * Uses producer-consumer pattern with scheduled execution.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class IngestionScheduler {

    private final TickProducer tickProducer;
    private final TickConsumer tickConsumer;

    @PostConstruct
    public void init() {
        log.info("IngestionScheduler started - producing ticks every 200ms");
    }

    /**
     * Produce ticks at a fixed rate (every 200ms = 5 ticks/second)
     */
    @Scheduled(fixedRateString = "${app.ingestion.produce-rate:200}")
    public void scheduledProduce() {
        tickProducer.produceTick();
    }

    /**
     * Consume and persist ticks at a fixed rate (every 500ms)
     * Batches multiple ticks together for efficient database writes
     */
    @Scheduled(fixedRateString = "${app.ingestion.consume-rate:500}")
    public void scheduledConsume() {
        tickConsumer.consumeTicks();
    }
}

