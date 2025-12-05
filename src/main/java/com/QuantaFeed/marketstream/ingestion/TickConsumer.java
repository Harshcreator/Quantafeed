package com.QuantaFeed.marketstream.ingestion;

import com.QuantaFeed.marketstream.model.Tick;
import com.QuantaFeed.marketstream.service.TickService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Consumes ticks from the shared blocking queue and persists them to the database.
 *
 * Consumer in the Producer-Consumer pattern.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TickConsumer {

    private final BlockingQueue<Tick> tickQueue;
    private final TickService tickService;
    private final AtomicLong persistedCount = new AtomicLong(0);

    /**
     * Drain available ticks from the queue and persist them to H2 database.
     * Uses batch insert for efficiency.
     */
    public void consumeTicks() {
        List<Tick> ticks = new ArrayList<>();
        tickQueue.drainTo(ticks);

        if (!ticks.isEmpty()) {
            List<Tick> saved = tickService.saveAllTicks(ticks);
            long total = persistedCount.addAndGet(saved.size());

            if (total % 50 == 0 || total <= 10) { // Log periodically
                log.info("Persisted batch of {} ticks to H2. Total persisted: {}",
                        saved.size(), total);
            }
        }
    }

    /**
     * Get total number of ticks persisted to database
     */
    public long getPersistedCount() {
        return persistedCount.get();
    }
}

