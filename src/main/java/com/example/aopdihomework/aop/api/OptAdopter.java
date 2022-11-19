package com.example.aopdihomework.aop.api;

import com.example.aopdihomework.aop.module.slack.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class OptAdopter {

    @Autowired
    private RestTemplate restTemplate;

    String getOtp() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("botToken");
        SendMessageRequest req = new SendMessageRequest();
        HttpEntity<SendMessageRequest> request1 = new HttpEntity<SendMessageRequest>(req, headers);
        String otpformhttp = restTemplate.exchange("www.otp.com", HttpMethod.POST, request1, String.class).getBody();
        return otpformhttp;
    }
}