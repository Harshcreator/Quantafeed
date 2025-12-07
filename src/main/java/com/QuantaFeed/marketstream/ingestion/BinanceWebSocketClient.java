package com.QuantaFeed.marketstream.ingestion;

import com.QuantaFeed.marketstream.config.BinanceWebSocketProperties;
import com.QuantaFeed.marketstream.model.Tick;
import com.QuantaFeed.marketstream.model.binance.BinanceTradeEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;

/**
 * Connects to Binance public WebSocket stream and pushes incoming trades
 * into the tick producer queue.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BinanceWebSocketClient {

    private final BinanceWebSocketProperties properties;
    private final TickProducer tickProducer;
    private final ObjectMapper objectMapper;

    private final ReactorNettyWebSocketClient webSocketClient = new ReactorNettyWebSocketClient();

    @PostConstruct
    public void start() {
        if (!properties.isEnabled()) {
            log.info("Binance WebSocket ingestion is disabled via configuration.");
            return;
        }

        String url = properties.getBaseUrl() + "/" + properties.getStream();
        URI uri = URI.create(url);

        log.info("Connecting to Binance WebSocket stream: {}", url);

        webSocketClient.execute(uri, session ->
                session.receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .flatMap(this::handleMessage)
                        .then()
        ).doOnError(error ->
                log.error("Error in Binance WebSocket connection", error)
        ).subscribe();
    }

    private Mono<Void> handleMessage(String payload) {
        try {
            BinanceTradeEvent event = objectMapper.readValue(payload, BinanceTradeEvent.class);

            if (event.getSymbol() == null || event.getPrice() == null || event.getTradeTime() == null) {
                // Ignore non-trade messages or malformed events
                return Mono.empty();
            }

            String symbol = event.getSymbol();
            BigDecimal price = event.priceAsBigDecimal();
            Instant timestamp = Instant.ofEpochMilli(event.getTradeTime());

            Tick tick = new Tick(symbol, price, timestamp);
            tickProducer.publishTick(tick);

        } catch (Exception e) {
            log.error("Failed to process Binance payload: {}", payload, e);
        }
        return Mono.empty();
    }
}

