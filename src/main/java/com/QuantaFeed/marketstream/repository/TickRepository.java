package com.QuantaFeed.marketstream.repository;

import com.QuantaFeed.marketstream.model.Tick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TickRepository extends JpaRepository<Tick, Long> {

    /**
     * Find the latest tick for a given symbol
     */
    Optional<Tick> findTopBySymbolOrderByTimestampDesc(String symbol);

    /**
     * Find all ticks for a symbol within a time range
     */
    List<Tick> findBySymbolAndTimestampBetweenOrderByTimestampDesc(
            String symbol, Instant start, Instant end);

    /**
     * Find all ticks within a time range
     */
    List<Tick> findByTimestampBetweenOrderByTimestampDesc(Instant start, Instant end);

    /**
     * Find recent ticks for a symbol with limit
     */
    List<Tick> findTop100BySymbolOrderByTimestampDesc(String symbol);

    /**
     * Find ticks for OHLC calculation (last N minutes)
     */
    List<Tick> findBySymbolAndTimestampAfterOrderByTimestampAsc(String symbol, Instant after);
}

