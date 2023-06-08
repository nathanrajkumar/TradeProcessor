package com.nathanrajkumar.scotiabank.TradeProcessor.config.kafka;

import com.nathanrajkumar.scotiabank.TradeProcessor.model.Message;
import org.springframework.kafka.support.serializer.JsonSerializer;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.PracticalAdvice;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.TradeMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.PropertyAccessException;
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
    /**
     * Host and port Kafka is running on
     */
//    @Value("${kafka.bootstrap-servers}")
//    private String bootstrapServers;

    @Autowired
    private KafkaProperties kafkaProperties;


    /**
     * configuration for kafka server
     * @return
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        //props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /**
     * creates Kafka producer instances
     * @return ProducerFactory<String, String>
     */
    @Bean
    public ProducerFactory<String, TradeMessage> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Kafka Template sends the message to the respective topic, this is the end result
     * @return KafkaTemplate<String, String>
     */
    @Bean
    public KafkaTemplate<String, TradeMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
