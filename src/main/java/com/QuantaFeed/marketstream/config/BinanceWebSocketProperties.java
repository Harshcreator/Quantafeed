package com.QuantaFeed.marketstream.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "binance.websocket")
public class BinanceWebSocketProperties {

    /** Enable or disable Binance WebSocket ingestion */
    private boolean enabled = true;

    /** Base WebSocket URL, e.g. wss://stream.binance.com:9443/ws */
    private String baseUrl = "wss://stream.binance.com:9443/ws";

    /** Stream path, e.g. btcusdt@trade */
    private String stream = "btcusdt@trade";

    /** Symbol used in our domain model, e.g. BTCUSDT */
    private String symbol = "BTCUSDT";
}
