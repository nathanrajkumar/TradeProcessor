package com.nathanrajkumar.scotiabank.TradeProcessor.mapper;

import com.nathanrajkumar.scotiabank.TradeProcessor.downstream.DownstreamAPISecurityID;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.IdSource;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.TradeMessage;
import lombok.NoArgsConstructor;
import quickfix.*;
import quickfix.field.*;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class TradeMessageMapper {

    private DataDictionary dataDictionary44;
    private DataDictionary dataDictionary50;
    private DefaultMessageFactory messageFactory;
    private DownstreamAPISecurityID downstreamAPISecurityID;

    public Message mapToFixMessage(String message)  {
        try {
            List<String> splitStrings = Arrays.stream(message.split("\u0001")).toList();
            String fixVersion = splitStrings.get(0).split("=")[1];
            if (fixVersion.equals("FIX.4.4")) {
                messageFactory = new DefaultMessageFactory(ApplVerID.FIX44);
                dataDictionary44 = new DataDictionary("FIX44.xml");
                return MessageUtils.parse(messageFactory, dataDictionary44, message);
            } else {
                messageFactory = new DefaultMessageFactory(ApplVerID.FIX50);
                dataDictionary50 = new DataDictionary("FIX50.xml");
                return MessageUtils.parse(messageFactory, dataDictionary50, message);
            }
        } catch (InvalidMessage e) {
            throw new RuntimeException(e);
        } catch (ConfigError e) {
            throw new RuntimeException(e);
        }
    }

    public TradeMessage mapToTradeMessage(Message message) {
        TradeMessage tradeMessage = new TradeMessage();
        try {
            tradeMessage.setTradeId(message.isSetField(new TradeID()) == true ? message.getField(new TradeID()).getValue() : null);
            //tradeMessage.setSecurityId(message.isSetField(new SecurityID()) == true ? message.getField(new SecurityID()).getValue() : null);
            tradeMessage.setPrice(message.isSetField(new Price()) == true ? message.getField(new Price()).getValue() : null);
            tradeMessage.setAccount(message.isSetField(new Account()) == true ? message.getField(new Account()).getValue(): null);
            if (message.isSetField(new SecurityIDSource()) == true) {
                switch (message.getField(new SecurityIDSource()).getValue()) {
                    case "1":
                        tradeMessage.setIdSource(IdSource.CUSIP);
                    case "2":
                        tradeMessage.setIdSource(IdSource.SEDOL);
                    case "4":
                        tradeMessage.setIdSource(IdSource.ISIN);
                    case "5":
                        tradeMessage.setIdSource(IdSource.RIC);
                }
            }
            tradeMessage.setQty(message.isSetField(new Quantity()) == true ? (int) message.getField(new Quantity()).getValue() : null);
            //tradeMessage.setTicker(message.isSetField(new Symbol()) == true ? message.getField(new Symbol()).getValue() : null);
            TradeMessage updatedTradeMessage = downstreamAPISecurityID.callDownstreamSecurityIDEndpoint(tradeMessage);
            return updatedTradeMessage;

        } catch (FieldNotFound e) {
            throw new RuntimeException(e);
        }
    };


}
