package com.QuantaFeed.marketstream.model.binance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Minimal representation of a Binance trade event from the @trade stream.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceTradeEvent {

    /** Symbol, e.g. BTCUSDT */
    @JsonProperty("s")
    private String symbol;

    /** Price as string, e.g. "42251.23" */
    @JsonProperty("p")
    private String price;

    /** Trade time in milliseconds since epoch */
    @JsonProperty("T")
    private Long tradeTime;

    public BigDecimal priceAsBigDecimal() {
        return new BigDecimal(price);
    }
}
