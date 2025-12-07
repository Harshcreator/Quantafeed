package com.QuantaFeed.marketstream.kafka;

import com.QuantaFeed.marketstream.model.Tick;
import com.QuantaFeed.marketstream.service.TickService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TickKafkaConsumer {

    private final TickService tickService;

    @KafkaListener(topics = "ticks", groupId = "tickvault-consumers")
    public void consume(TickMessage message) {
        if (message == null) {
            log.warn("Received null TickMessage from Kafka");
            return;
        }
        Tick tick = new Tick(message.getSymbol(), message.getPrice(), message.getTimestamp());
        tickService.saveTick(tick);
    }
}

