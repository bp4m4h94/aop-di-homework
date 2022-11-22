package com.example.aopdihomework.aop.service;

import com.example.aopdihomework.aop.module.slack.SendMessageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class NotifocationAdapterImpl {
    public NotifocationAdapterImpl() {
    }

    void sendNotification(RestTemplate restTemplate1, String slackUrl1) {
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        SendMessageRequest req1 = new SendMessageRequest();
        req1.setText("Hello, notify slack");
        HttpEntity<SendMessageRequest> request2 = new HttpEntity<SendMessageRequest>(req1, headers1);
        restTemplate1.exchange(slackUrl1, HttpMethod.POST, request2, String.class);
    }
}