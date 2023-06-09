package com.nathanrajkumar.scotiabank.TradeProcessor.service;

import com.nathanrajkumar.scotiabank.TradeProcessor.mapper.TradeMessageMapper;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.SecurityId;
import com.nathanrajkumar.scotiabank.TradeProcessor.model.TradeMessage;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SecurityIDDownstreamAPIService {

    public RestTemplate restTemplate;

    private TradeMessageMapper mapper;

    public SecurityIDDownstreamAPIService() {
        restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(getJsonMessageConverters());
        mapper = new TradeMessageMapper();
    }

    public TradeMessage callDownstreamSecurityIDEndpoint(TradeMessage tradeMessage) {
        SecurityId securityId = new SecurityId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<SecurityId> entity = new HttpEntity<>(securityId, headers);
        // TODO: this endpoint does not lead anywhere.  For now, I will not call this until verfication of endpoint
        ResponseEntity<SecurityId> response = restTemplate.exchange(
                "https://sec-master.bns/find", HttpMethod.POST, entity, SecurityId.class);
        return mapper.mapDownstreamResponseToTradeMessage(tradeMessage, response.getBody());
    }

    private List<HttpMessageConverter<?>> getJsonMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());
        return converters;
    }


}
