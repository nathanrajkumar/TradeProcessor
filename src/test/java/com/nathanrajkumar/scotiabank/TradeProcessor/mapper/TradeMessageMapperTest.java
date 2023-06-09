package com.nathanrajkumar.scotiabank.TradeProcessor.mapper;

import org.junit.jupiter.api.Test;
import quickfix.ConfigError;
import quickfix.Message;

import static org.junit.jupiter.api.Assertions.*;

class TradeMessageMapperTest {

    String executionReport = "8=FIX.4.4\u00019=289\u000135=8\u000134=1090\u000149=TESTSELL1\u000152=20180920-18:23:53.671\u000156=TESTBUY1\u00011003=12345\u00016=113.35\u000111=636730640278898634\u000114=3500.0000000000\u000115=USD\u000117=20636730646335310000\u000121=2\u000131=113.35\u000132=3500\u000137=20636730646335310000\u000138=7000\u000139=1\u000140=1\u000154=1\u000155=MSFT\u000160=20180920-18:23:53.531\u0001150=F\u0001151=3500\u0001453=1\u0001448=BRK2\u0001447=D\u0001452=1\u000110=152\u0001";
    String fixMessageString = "8=FIX.4.2\u00019=251\u000135=D\u000149=AFUNDMGR\u000156=ABROKER\u000134=2\u000152=2003061501:14:49\u000111=12345\u00011=111111\u000163=0\u000164=20030621\u000121=3\u0001110=1000\u0001111=50000\u000155=IBM\u000148=459200101\u000122=1\u000154=1\u000160=2003061501:14:49\u000138=5000\u000140=1\u000144=15.75\u000115=USD\u000159=0\u000110=176\u0001";

    void mapToFixMessageTest() throws ConfigError {
        TradeMessageMapper parser = new TradeMessageMapper();

        Message message = parser.mapToFixMessage(fixMessageString);
        //parser.mapToTradeMessage(message);


    }
}