package com.QuantaFeed.marketstream.ingestion;

import com.QuantaFeed.marketstream.model.Tick;
import com.QuantaFeed.marketstream.service.TickService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Consumes ticks from the shared blocking queue and persists them to the database.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TickConsumer {

    private final BlockingQueue<Tick> tickQueue;
    private final TickService tickService;

    /**
     * Drain available ticks from the queue and persist them
     */
    public void consumeTicks() {
        List<Tick> ticks = new ArrayList<>();
        tickQueue.drainTo(ticks);

        if (!ticks.isEmpty()) {
            tickService.saveAllTicks(ticks);
            log.debug("Consumed and persisted {} ticks", ticks.size());
        }
    }
}

