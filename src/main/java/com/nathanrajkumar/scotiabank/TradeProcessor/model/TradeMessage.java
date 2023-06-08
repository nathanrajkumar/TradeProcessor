package com.nathanrajkumar.scotiabank.TradeProcessor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the message we send to the front end via Kafka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeMessage {

    private String tradeId;
    private String account;
    private SecurityId securityId;
    private IdSource idSource;
    private Integer qty;
    private Double price;
}
