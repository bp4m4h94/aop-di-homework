package com.example.aopdihomework.aop.adapter.impl;

import org.springframework.web.client.RestTemplate;

public interface NotificationAdapter {

    void sendNotification(RestTemplate restTemplate1, String slackUrl1);
}
