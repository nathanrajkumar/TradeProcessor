package com.nathanrajkumar.scotiabank.TradeProcessor.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
* Model to populate fields from rest call
* */
@NoArgsConstructor
@Data
public class SecurityId {
    private String ric;
    private String isin;
    private String cusip;
    private String sedol;
    private String ticker;
    private String name;
}
