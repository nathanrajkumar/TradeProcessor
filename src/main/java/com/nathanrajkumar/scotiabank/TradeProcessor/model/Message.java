package com.nathanrajkumar.scotiabank.TradeProcessor.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

public record Message(@JsonProperty("message") String msg) {

}
