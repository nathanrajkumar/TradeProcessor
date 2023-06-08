package com.nathanrajkumar.scotiabank.TradeProcessor.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class TradeMessageSender {

    @Autowired
    private KafkaTemplate<String, TradeMessage> kafkaTemplate;

    @Autowired
    public TradeMessageSender(KafkaTemplate<String, TradeMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * he sendDefault API requires that a default topic has been provided to the template.
     * @param key
     * @param msg
     */
    public void sendTradeMessageToKafka(String key, TradeMessage msg) {
        CompletableFuture<SendResult<String, TradeMessage>> future = kafkaTemplate.sendDefault(key, msg) ;
//        CompletableFuture<SendResult<K, V>> sendDefault(V data);
//        CompletableFuture<SendResult<K, V>> sendDefault(K key, V data);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                //handleSuccess(data);
            }
            else {
                //  handleFailure(data, record, ex);
            }
        });
    }
}
