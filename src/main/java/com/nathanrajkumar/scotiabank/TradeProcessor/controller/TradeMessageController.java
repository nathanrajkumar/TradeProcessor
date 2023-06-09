package com.nathanrajkumar.scotiabank.TradeProcessor.controller;

import com.nathanrajkumar.scotiabank.TradeProcessor.service.TradeMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradeMessageController {

    private static final Logger logger =
            LoggerFactory.getLogger(TradeMessageController.class);

    @Autowired
    private TradeMessageSender sender;


    /**
     * this is used primarily for testing purposes as read world application of this would most likely come from a
     * UNIX server of some kind.  In this case, we call the post endpoint with fix message as a url encoded query
     * parameter
     * @param message
     * @throws Exception
     */
    @PostMapping("/publish")
    public void sendMessageToKafkaTopic(@RequestParam("fixMessage") String message) throws Exception {
        logger.info("Calling Kafka Listener...");
        sender.sendMessageToKafkaTopic(message);

    }

    /**
     * This is a commented out kafka listener used to handle fix message strings coming from a UNIX server.  It does
     * the same thing as the post call but instead uses the Kafka listener to listen for changes on the Kafka topic
     */
//    @KafkaListener(topics = "${trade-processor.topic-name}", clientIdPrefix = "json", containerFactory = "kafkaListenerContainerFactory")
//    public void onMessage(ConsumerRecord<String, String> cr,
//                          @Payload String payload) throws ConfigError, InvalidMessage, FieldNotFound {
//        logger.info("Calling Kafka Listener...");
//        sender.sendMessageToKafkaTopic(payload);
//    }

}
