package com.nathanrajkumar.scotiabank.TradeProcessor.controller;

import com.nathanrajkumar.scotiabank.TradeProcessor.mapper.TradeMessageMapper;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.PracticalAdvice;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.TradeMessage;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.TradeMessageSender;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import quickfix.*;
import quickfix.field.ExecID;
import quickfix.field.MsgType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@RestController
public class TradeMessageController {

    private static final Logger logger =
            LoggerFactory.getLogger(TradeMessageController.class);

    @Autowired
    private final KafkaTemplate<String, TradeMessage> template;

    private final String topicName;
    private final int messagesPerRequest;
    private CountDownLatch latch;


    public TradeMessageController(
            final KafkaTemplate<String, TradeMessage> template,
            @Value("${trade-processor.topic-name}") final String topicName,
            @Value("${trade-processor.messages-per-request}") final int messagesPerRequest) {
        this.template = template;
        this.topicName = topicName;
        this.messagesPerRequest = messagesPerRequest;

    }

    @GetMapping("/hello")
    public String hello() throws Exception {
        latch = new CountDownLatch(messagesPerRequest);
//        IntStream.range(0, messagesPerRequest)
//                .forEach(i -> {
//                    this.template.send(topicName, String.valueOf(i),
//                            "8=FIX.4.2<SOH>9=251<SOH>35=D<SOH>49=AFUNDMGR<SOH>56=ABROKER<SOH>34=2<SOH>52=2003061501:14:49<SOH>11=12345<SOH>1=111111<SOH>63=0<SOH>64=20030621<SOH>21=3<SOH>110=1000<SOH>111=50000<SOH>55=IBM<SOH>48=459200101<SOH>22=1<SOH>54=1<SOH>60=2003061501:14:49<SOH>38=5000<SOH>40=1<SOH>44=15.75<SOH>15=USD<SOH>59=0<SOH>10=127<SOH>".trim()
//                    );
//                });
        latch.await(60, TimeUnit.SECONDS);
        logger.info("All messages received");
        return "Hello Kafka!";
    }
    @KafkaListener(topics = "${trade-processor.topic-name}", clientIdPrefix = "json", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(ConsumerRecord<String, String> cr,
                               @Payload String payload) throws ConfigError, InvalidMessage, FieldNotFound {

        TradeMessageMapper mapper = new TradeMessageMapper();
        Message message = mapper.mapToFixMessage(payload);
        TradeMessage tradeMessage = mapper.mapToTradeMessage(message);
        this.template.send(topicName, tradeMessage);

        logger.info("Logger 1 [JSON] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
                typeIdHeader(cr.headers()), payload, cr.toString());
        latch.countDown();
    }

    private static String typeIdHeader(Headers headers) {
        return StreamSupport.stream(headers.spliterator(), false)
                .filter(header -> header.key().equals("__TypeId__"))
                .findFirst().map(header -> new String(header.value())).orElse("N/A");
    }
}
