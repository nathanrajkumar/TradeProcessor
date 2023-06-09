package com.nathanrajkumar.scotiabank.TradeProcessor.service;

import com.nathanrajkumar.scotiabank.TradeProcessor.mapper.TradeMessageMapper;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.TradeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import quickfix.Message;

@Service
public class TradeMessageSender {


    Logger log = LoggerFactory.getLogger(TradeMessageSender.class);

    @Autowired
    private KafkaTemplate<String, TradeMessage> kafkaTemplate;

    @Value("${trade-processor.topic-name}")
    private String topicName;

    @Autowired
    public TradeMessageSender(KafkaTemplate<String, TradeMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Creates Fix Message object, maps to Trade Message then asyncronously sends to Kafka topic.
     * @param fixMessage
     */
    public void sendMessageToKafkaTopic(String fixMessage) {
        TradeMessageMapper mapper = new TradeMessageMapper();
        Message message = mapper.mapToFixMessage(fixMessage);
        TradeMessage tradeMessage = mapper.mapToTradeMessage(message);
        this.kafkaTemplate.send(topicName, tradeMessage)
            .whenComplete((result, ex) -> {
                if(ex == null) {
                    log.info("Message --> " + result + " sent successfully to " + topicName);
                } else {
                    log.info("Message send failure to " + topicName + " --> ", ex);
                }
            }
        );
    }
}
