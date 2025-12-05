package com.QuantaFeed.marketstream.service;

import com.QuantaFeed.marketstream.model.Tick;
import com.QuantaFeed.marketstream.repository.TickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.QuantaFeed.marketstream.model.OhlcData;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TickService {

    private final TickRepository tickRepository;

    /**
     * Save a tick to the database
     */
    public Tick saveTick(Tick tick) {
        return tickRepository.save(tick);
    }

    /**
     * Save multiple ticks to the database
     */
    public List<Tick> saveAllTicks(List<Tick> ticks) {
        return tickRepository.saveAll(ticks);
    }

    /**
     * Get the latest tick for a symbol
     */
    public Optional<Tick> getLatestTick(String symbol) {
        return tickRepository.findTopBySymbolOrderByTimestampDesc(symbol);
    }

    /**
     * Get historical ticks for a symbol within a time range
     */
    public List<Tick> getHistoricalTicks(String symbol, Instant start, Instant end) {
        return tickRepository.findBySymbolAndTimestampBetweenOrderByTimestampDesc(symbol, start, end);
    }

    /**
     * Get all ticks within a time range
     */
    public List<Tick> getAllTicksInRange(Instant start, Instant end) {
        return tickRepository.findByTimestampBetweenOrderByTimestampDesc(start, end);
    }

    /**
     * Get recent ticks for a symbol
     */
    public List<Tick> getRecentTicks(String symbol) {
        return tickRepository.findTop100BySymbolOrderByTimestampDesc(symbol);
    }

    /**
     * Calculate 1-minute OHLC aggregations for a symbol (last 10 minutes)
     */
    public List<OhlcData> getOhlcData(String symbol) {
        Instant tenMinutesAgo = Instant.now().minus(10, ChronoUnit.MINUTES);
        List<Tick> ticks = tickRepository.findBySymbolAndTimestampAfterOrderByTimestampAsc(symbol, tenMinutesAgo);

        if (ticks.isEmpty()) {
            return new ArrayList<>();
        }

        // Group ticks by minute
        Map<Instant, List<Tick>> ticksByMinute = ticks.stream()
                .collect(Collectors.groupingBy(tick -> tick.getTimestamp().truncatedTo(ChronoUnit.MINUTES)));

        // Calculate OHLC for each minute
        return ticksByMinute.entrySet().stream()
                .map(entry -> calculateOhlc(symbol, entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(OhlcData::getMinuteStart))
                .collect(Collectors.toList());
    }

    private OhlcData calculateOhlc(String symbol, Instant minuteStart, List<Tick> ticks) {
        List<Tick> sorted = ticks.stream()
                .sorted(Comparator.comparing(Tick::getTimestamp))
                .collect(Collectors.toList());

        BigDecimal open = sorted.get(0).getPrice();
        BigDecimal close = sorted.get(sorted.size() - 1).getPrice();
        BigDecimal high = sorted.stream().map(Tick::getPrice).max(Comparator.naturalOrder()).orElse(open);
        BigDecimal low = sorted.stream().map(Tick::getPrice).min(Comparator.naturalOrder()).orElse(open);

        return new OhlcData(symbol, minuteStart, open, high, low, close, (long) ticks.size());
    }
}

