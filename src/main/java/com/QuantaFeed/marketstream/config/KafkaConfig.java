package com.QuantaFeed.marketstream.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String TICKS_TOPIC = "ticks";

    @Bean
    public NewTopic ticksTopic() {
        return TopicBuilder.name(TICKS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
