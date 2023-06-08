package com.nathanrajkumar.scotiabank.TradeProcessor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TradeMessageReciever {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

//    @KafkaListener(
//            topics = "${trade-processor.topic-name}",
//            groupId="trade_message_processor")
//    void listener(Message message) {
////        System.out.println(message.toXML());
//        LOG.info("Message [{}]", message);
//    }


}
