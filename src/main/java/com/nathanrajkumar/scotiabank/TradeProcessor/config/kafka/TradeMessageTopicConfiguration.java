package com.nathanrajkumar.scotiabank.TradeProcessor.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TradeMessageTopicConfiguration {

    @Value("${trade-processor.topic-name}")
    private String topicName;
    @Bean
    public NewTopic tradeMessageTopic() {
        return new NewTopic(topicName, 3, (short) 1);
    }
}
