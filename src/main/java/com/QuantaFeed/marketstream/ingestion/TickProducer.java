package com.QuantaFeed.marketstream.ingestion;

import com.QuantaFeed.marketstream.model.Tick;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simulates live market data by producing random price ticks
 * and placing them into a shared blocking queue.
 * Producer in the Producer-Consumer pattern.
 */
@Component
@Slf4j
public class TickProducer {

    private final BlockingQueue<Tick> tickQueue;
    private final List<String> symbols;
    private final Random random = new Random();
    private final AtomicLong tickCount = new AtomicLong(0);

    // Base prices for each symbol (simulated starting prices)
    private final Map<String, Double> basePrices = new HashMap<>();

    public TickProducer(
            BlockingQueue<Tick> tickQueue,
            @Value("${app.symbols:AAPL,GOOG,MSFT,META,NVDA}") List<String> symbols) {
        this.tickQueue = tickQueue;
        this.symbols = symbols;
        initializeBasePrices();
    }

    private void initializeBasePrices() {
        // Initialize realistic starting prices for each symbol
        basePrices.put("AAPL", 178.50);
        basePrices.put("GOOG", 141.25);
        basePrices.put("MSFT", 378.90);
        basePrices.put("META", 505.75);
        basePrices.put("NVDA", 875.30);

        // Add default price for any unknown symbols
        for (String symbol : symbols) {
            basePrices.putIfAbsent(symbol, 100.0);
        }
    }

    /**
     * Generate a simulated tick for a random symbol.
     * Simulates realistic price movements using random walk.
     */
    public void produceTick() {
        int index = random.nextInt(symbols.size());
        String symbol = symbols.get(index);

        // Simulate price movement with random walk (-0.50 to +0.50)
        double currentPrice = basePrices.get(symbol);
        double priceChange = (random.nextDouble() - 0.5) * 1.0;
        double newPrice = Math.max(0.01, currentPrice + priceChange); // Ensure price stays positive

        // Update base price for next tick
        basePrices.put(symbol, newPrice);

        Tick tick = new Tick(
                symbol,
                BigDecimal.valueOf(newPrice).setScale(4, RoundingMode.HALF_UP),
                Instant.now()
        );

        try {
            boolean added = tickQueue.offer(tick);
            if (added) {
                long count = tickCount.incrementAndGet();
                if (count % 25 == 0) { // Log every 25 ticks (every 5 seconds at 200ms rate)
                    log.info("Produced {} ticks total. Latest: {} @ ${}",
                            count, tick.getSymbol(), tick.getPrice());
                }
            } else {
                log.warn("Queue full (capacity: {}), tick dropped: {}",
                        tickQueue.size(), tick.getSymbol());
            }
        } catch (Exception e) {
            log.error("Error producing tick for {}", symbol, e);
        }
    }

    /**
     * Get total number of ticks produced
     */
    public long getTickCount() {
        return tickCount.get();
    }
}

