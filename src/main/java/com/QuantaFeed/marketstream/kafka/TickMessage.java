package com.QuantaFeed.marketstream.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TickMessage {

    private String symbol;
    private BigDecimal price;
    private Instant timestamp;
}

