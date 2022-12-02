package com.example.aopdihomework.aop.adapter.impl;

import com.example.aopdihomework.aop.adapter.NotificationAdapter;
import com.example.aopdihomework.aop.module.Account;
import com.example.aopdihomework.aop.module.slack.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class NotificationAdapterImpl implements NotificationAdapter {

    @Autowired
    RestTemplate restTemplate;


    public NotificationAdapterImpl() {
    }

    @Override
    public void sendNotification(String account, String slackUrl1) {
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        SendMessageRequest req1 = new SendMessageRequest();
        req1.setText("Hello, " + account + "from slack");
        HttpEntity<SendMessageRequest> request2 = new HttpEntity<SendMessageRequest>(req1, headers1);
        restTemplate.exchange(slackUrl1, HttpMethod.POST, request2, String.class);
    }
}