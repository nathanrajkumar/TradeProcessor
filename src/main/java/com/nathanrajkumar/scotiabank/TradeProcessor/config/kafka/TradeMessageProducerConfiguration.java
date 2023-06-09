package com.nathanrajkumar.scotiabank.TradeProcessor.config.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.TradeMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class TradeMessageProducerConfiguration {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaProperties kafkaProperties;


    /**
     * configuration for kafka server
     * @return
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /**
     * creates Kafka producer instances
     * @return ProducerFactory<String, TradeMessage>
     */
    @Bean
    public ProducerFactory<String, TradeMessage> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Kafka Template sends the produicer record to topic trade_message, this is the end result
     * @return KafkaTemplate<String, TradeMessage>
     */
    @Bean
    public KafkaTemplate<String, TradeMessage> kafkaTemplate() {
        KafkaTemplate<String, TradeMessage> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
        kafkaTemplate.setDefaultTopic("trade_message");
        kafkaTemplate.setProducerListener(new ProducerListener<String, TradeMessage>() {
            @Override
            public void onSuccess(ProducerRecord<String, TradeMessage> producerRecord, RecordMetadata recordMetadata) {
                logger.info("ACK from ProducerListener message: {} offset:  {}", producerRecord.value(),
                        recordMetadata.offset());
            }
        });
        return kafkaTemplate;
    }
}
