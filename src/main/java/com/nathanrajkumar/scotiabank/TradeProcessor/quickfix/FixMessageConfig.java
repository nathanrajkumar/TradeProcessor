package com.nathanrajkumar.scotiabank.TradeProcessor.quickfix;

import org.springframework.stereotype.Component;


//public class FixMessageConfig extends MessageCracker implements quickfix.Application {
//
//    public void fromApp(Message message, SessionID sessionID)
//            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
//        crack(message, sessionID);
//    }
//
//    public void onMessage(Message msg, SessionID sessionID) {
//        String xmlMessage = msg.toXML();
//        System.out.println(xmlMessage);
//    }
//
//    @Override
//    public void onCreate(SessionID sessionID) {
//
//    }
//
//    @Override
//    public void onLogon(SessionID sessionID) {
//
//    }
//
//    @Override
//    public void onLogout(SessionID sessionID) {
//
//    }
//
//    @Override
//    public void toAdmin(Message message, SessionID sessionID) {
//
//    }
//
//    @Override
//    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
//
//    }
//
//    @Override
//    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
//
//    }
//}
