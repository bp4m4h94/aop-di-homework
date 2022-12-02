package com.example.aopdihomework.aop.adapter.impl;

import com.example.aopdihomework.aop.adapter.OptAdopter;
import com.example.aopdihomework.aop.module.slack.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class OptAdopterImpl implements OptAdopter {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getOtp(String account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("botToken");
        SendMessageRequest req = new SendMessageRequest();
        HttpEntity<SendMessageRequest> request1 = new HttpEntity<SendMessageRequest>(req, headers);
        return "opt123";
    }
}