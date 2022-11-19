package com.example.aopdihomework.aop.api;

import com.example.aopdihomework.aop.module.slack.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class FailedCounterAdapter {
    @Autowired
    private RestTemplate restTemplate;


    Integer getFailedCounter(HttpEntity<?> request) {
        return restTemplate.exchange("api/failedCounter/GetFailedCount", HttpMethod.POST, request, Integer.class).getBody();
    }

    void addFailedCounter(HttpEntity<SendMessageRequest> request2) {
    }

    void resetFailedCounter(HttpEntity<?> request) {
        restTemplate.exchange("api/failedCounter/Reset", HttpMethod.POST, request, String.class);
    }
}