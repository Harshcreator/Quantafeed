package com.QuantaFeed.marketstream.ingestion;

import com.QuantaFeed.marketstream.model.Tick;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Simulates live market data by producing random price ticks
 * and placing them into a shared blocking queue.
 */
@Component
@Slf4j
public class TickProducer {

    private final BlockingQueue<Tick> tickQueue;
    private final List<String> symbols;
    private final Random random = new Random();

    // Base prices for each symbol (simulated starting prices)
    private final double[] basePrices = {150.0, 2800.0, 350.0, 180.0, 450.0};

    public TickProducer(
            BlockingQueue<Tick> tickQueue,
            @Value("${app.symbols:AAPL,GOOG,MSFT,META,NVDA}") List<String> symbols) {
        this.tickQueue = tickQueue;
        this.symbols = symbols;
    }

    /**
     * Generate a simulated tick for a random symbol
     */
    public void produceTick() {
        int index = random.nextInt(symbols.size());
        String symbol = symbols.get(index);

        // Simulate price movement with random walk
        double basePrice = basePrices[index % basePrices.length];
        double priceChange = (random.nextDouble() - 0.5) * 2.0; // -1.0 to +1.0
        double newPrice = basePrice + priceChange;

        // Update base price for next tick
        basePrices[index % basePrices.length] = newPrice;

        Tick tick = new Tick(
                symbol,
                BigDecimal.valueOf(newPrice).setScale(4, RoundingMode.HALF_UP),
                Instant.now()
        );

        try {
            boolean added = tickQueue.offer(tick);
            if (added) {
                log.debug("Produced tick: {} @ {}", tick.getSymbol(), tick.getPrice());
            } else {
                log.warn("Queue full, tick dropped: {}", tick.getSymbol());
            }
        } catch (Exception e) {
            log.error("Error producing tick", e);
        }
    }
}

