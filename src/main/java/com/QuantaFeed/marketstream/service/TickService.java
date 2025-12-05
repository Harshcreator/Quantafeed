package com.QuantaFeed.marketstream.service;

import com.QuantaFeed.marketstream.model.Tick;
import com.QuantaFeed.marketstream.repository.TickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}

