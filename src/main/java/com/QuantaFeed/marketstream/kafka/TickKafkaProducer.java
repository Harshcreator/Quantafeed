package com.QuantaFeed.marketstream.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TickKafkaProducer {

    private final KafkaTemplate<String, TickMessage> kafkaTemplate;

    public void sendTick(TickMessage message) {
        if (message == null) {
            log.warn("Attempted to send null TickMessage to Kafka");
            return;
        }
        kafkaTemplate.send("ticks", message.getSymbol(), message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send tick to Kafka for symbol {}", message.getSymbol(), ex);
                    }
                });
    }
}

