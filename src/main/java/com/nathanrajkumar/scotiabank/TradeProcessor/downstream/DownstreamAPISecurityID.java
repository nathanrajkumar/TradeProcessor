package com.nathanrajkumar.scotiabank.TradeProcessor.downstream;

import com.nathanrajkumar.scotiabank.TradeProcessor.model.SecurityId;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.TradeMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Data
public class DownstreamAPISecurityID {

    @Autowired
    public RestTemplate restTemplate;

    public TradeMessage callDownstreamSecurityIDEndpoint(TradeMessage tradeMessage) {
        SecurityId securityId = new SecurityId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<SecurityId> entity = new HttpEntity<>(securityId, headers);

        ResponseEntity<SecurityId> response = restTemplate.exchange(
                "https://sec-master.bns/find", HttpMethod.POST, entity, SecurityId.class);
        tradeMessage.setSecurityId(response.getBody());
        return tradeMessage;
    }


}
