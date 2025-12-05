package com.QuantaFeed.marketstream.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * OHLC (Open-High-Low-Close) aggregation data transfer object.
 * Represents 1-minute candlestick data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OhlcData {

    private String symbol;
    private Instant minuteStart;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private Long tickCount;
}