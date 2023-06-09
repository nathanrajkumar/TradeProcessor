package com.nathanrajkumar.scotiabank.TradeProcessor.mapper;

import com.nathanrajkumar.scotiabank.TradeProcessor.model.SecurityId;
import com.nathanrajkumar.scotiabank.TradeProcessor.service.SecurityIDDownstreamAPIService;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.IdSource;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.TradeMessage;
import lombok.NoArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import quickfix.*;
import quickfix.field.*;

import java.util.regex.Pattern;


@NoArgsConstructor
public class TradeMessageMapper {

    private DataDictionary dataDictionary44;
    private DataDictionary dataDictionary50;
    private DefaultMessageFactory messageFactory;

    private SecurityIDDownstreamAPIService downstreamAPISecurityID;

    /**
     * To create a FIX message I pulled in a library QuickFix/J which contains all the message parsing and validation needed
     * for a FIX message.  I leveraged this to create the fix message and attach a data dictionary for both 4.4 and 5.0 versions of
     * quickfix as 5.0 had the tag=value for tradeId.  This returns a Message object from QuickFix
     * @param message
     * @return Message
     */
    public Message mapToFixMessage(String message)  {
        try {
            Pattern p = Pattern.compile("\u0001", Pattern.LITERAL);
            String[] splitStrings = p.split(message);
            String fixVersion = splitStrings[0].split("=")[1];
            // this is for the url encoded parameter.  The \u0001 wasnt recognized as Java converts this into
            // a string literal which does not pass the FIX message validation as FIX needs a delimiter.
            // used Apache's StringEscapeUtils to escape the java translation
            String translatedString = StringEscapeUtils.unescapeJava(message);
            if (fixVersion.equals("FIX.4.4")) {
                messageFactory = new DefaultMessageFactory(ApplVerID.FIX44);
                dataDictionary44 = new DataDictionary("FIX44.xml");
                return MessageUtils.parse(messageFactory, dataDictionary44, translatedString);
            } else {
                messageFactory = new DefaultMessageFactory(ApplVerID.FIX50);
                dataDictionary50 = new DataDictionary("FIX50.xml");
                return MessageUtils.parse(messageFactory, dataDictionary50, translatedString);
            }
        } catch (InvalidMessage e) {
            throw new RuntimeException(e);
        } catch (ConfigError e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This uses quickfix's data types that are extensions of the tag=value key pairs found in the data dictonary (i.e TradeId() has a tag value
     * of 1003 and it extends quickfix StringField for string validation).
     *
     * I commented out the rest call here because the endpoint wasnt found.  I have created the skeleton for calling and and
     * mapping the downstream response.
     *
     * Returns a mapped Trade Message
     * @param message
     * @return TradeMessage
     */
    public TradeMessage mapToTradeMessage(Message message) {
        TradeMessage tradeMessage = new TradeMessage();
        try {
            tradeMessage.setTradeId(message.isSetField(new TradeID()) ? message.getField(new TradeID()).getValue() : null);
            tradeMessage.setPrice(message.isSetField(new Price()) ? message.getField(new Price()).getValue() : null);
            tradeMessage.setAccount(message.isSetField(new Account()) ? message.getField(new Account()).getValue(): null);
            if (message.isSetField(new SecurityIDSource())) {
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
            /*
                Commented this out as I couldn't connect to the endpoint since it was most likely a mock endpoint.
             */
//          tradeMessage.setTicker(message.isSetField(new Symbol()) == true ? message.getField(new Symbol()).getValue() : null);
//          downstreamAPISecurityID = new SecurityIDDownstreamAPIService();
//          TradeMessage updatedTradeMessage = downstreamAPISecurityID.callDownstreamSecurityIDEndpoint(tradeMessage);
//          return updatedTradeMessage;
            return tradeMessage;

        } catch (FieldNotFound e) {
            throw new RuntimeException(e);
        }
    };

    public TradeMessage mapDownstreamResponseToTradeMessage(TradeMessage tradeMessage, SecurityId response) {
        SecurityId securityId = new SecurityId();
        securityId.setCusip(response.getCusip());
        securityId.setName(response.getName());
        securityId.setIsin(response.getIsin());
        securityId.setRic(response.getRic());
        securityId.setTicker(response.getTicker());
        securityId.setSedol(response.getSedol());
        tradeMessage.setSecurityId(securityId);
        return tradeMessage;
    }


}
