package com.QuantaFeed.marketstream.ingestion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler that coordinates tick production and consumption.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class IngestionScheduler {

    private final TickProducer tickProducer;
    private final TickConsumer tickConsumer;

    /**
     * Produce ticks at a fixed rate (every 100ms = 10 ticks/second)
     */
    @Scheduled(fixedRateString = "${app.ingestion.produce-rate:100}")
    public void scheduledProduce() {
        tickProducer.produceTick();
    }

    /**
     * Consume and persist ticks at a fixed rate (every 500ms)
     */
    @Scheduled(fixedRateString = "${app.ingestion.consume-rate:500}")
    public void scheduledConsume() {
        tickConsumer.consumeTicks();
    }
}

