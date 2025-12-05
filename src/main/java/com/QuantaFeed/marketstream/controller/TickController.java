package com.QuantaFeed.marketstream.controller;

import com.QuantaFeed.marketstream.model.Tick;
import com.QuantaFeed.marketstream.service.TickService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/ticks")
@RequiredArgsConstructor
public class TickController {

    private final TickService tickService;

    /**
     * Get the latest tick for a symbol
     * GET /api/ticks/latest/{symbol}
     */
    @GetMapping("/latest/{symbol}")
    public ResponseEntity<Tick> getLatestTick(@PathVariable String symbol) {
        return tickService.getLatestTick(symbol.toUpperCase())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get historical ticks for a symbol within a time range
     * GET /api/ticks/history?symbol=AAPL&start=2024-01-01T00:00:00Z&end=2024-01-02T00:00:00Z
     */
    @GetMapping("/history")
    public ResponseEntity<List<Tick>> getHistoricalTicks(
            @RequestParam String symbol,
            @RequestParam Instant start,
            @RequestParam Instant end) {
        List<Tick> ticks = tickService.getHistoricalTicks(symbol.toUpperCase(), start, end);
        return ResponseEntity.ok(ticks);
    }

    /**
     * Get recent ticks for a symbol (last 100)
     * GET /api/ticks/recent/{symbol}
     */
    @GetMapping("/recent/{symbol}")
    public ResponseEntity<List<Tick>> getRecentTicks(@PathVariable String symbol) {
        List<Tick> ticks = tickService.getRecentTicks(symbol.toUpperCase());
        return ResponseEntity.ok(ticks);
    }
}

