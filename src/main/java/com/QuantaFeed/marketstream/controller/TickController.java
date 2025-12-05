package com.QuantaFeed.marketstream.controller;

 import com.QuantaFeed.marketstream.model.OhlcData;
 import com.QuantaFeed.marketstream.model.Tick;
 import com.QuantaFeed.marketstream.service.TickService;
 import lombok.RequiredArgsConstructor;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;

 import java.time.Instant;
 import java.util.List;

 @RestController
 @RequestMapping("/ticks")
 @RequiredArgsConstructor
 public class TickController {

     private final TickService tickService;

     /**
      * Get the latest tick for a symbol
      * GET /ticks/latest?symbol=XYZ
      */
     @GetMapping("/latest")
     public ResponseEntity<Tick> getLatestTick(@RequestParam String symbol) {
         return tickService.getLatestTick(symbol.toUpperCase())
                 .map(ResponseEntity::ok)
                 .orElse(ResponseEntity.notFound().build());
     }

     /**
      * Get historical ticks for a symbol within a time range
      * GET /ticks/history?symbol=XYZ&from=2024-01-01T00:00:00Z&to=2024-01-02T00:00:00Z
      */
     @GetMapping("/history")
     public ResponseEntity<List<Tick>> getHistoricalTicks(
             @RequestParam String symbol,
             @RequestParam Instant from,
             @RequestParam Instant to) {
         List<Tick> ticks = tickService.getHistoricalTicks(symbol.toUpperCase(), from, to);
         return ResponseEntity.ok(ticks);
     }

     /**
      * Get 1-minute OHLC aggregations for a symbol (last 10 minutes)
      * GET /ticks/ohlc?symbol=XYZ
      */
     @GetMapping("/ohlc")
     public ResponseEntity<List<OhlcData>> getOhlcData(@RequestParam String symbol) {
         List<OhlcData> ohlcData = tickService.getOhlcData(symbol.toUpperCase());
         return ResponseEntity.ok(ohlcData);
     }

     /**
      * Get recent ticks for a symbol (last 100)
      * GET /ticks/recent?symbol=XYZ
      */
     @GetMapping("/recent")
     public ResponseEntity<List<Tick>> getRecentTicks(@RequestParam String symbol) {
         List<Tick> ticks = tickService.getRecentTicks(symbol.toUpperCase());
         return ResponseEntity.ok(ticks);
     }
 }