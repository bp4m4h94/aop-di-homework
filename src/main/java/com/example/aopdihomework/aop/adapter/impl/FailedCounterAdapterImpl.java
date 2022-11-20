package com.example.aopdihomework.aop.adapter.impl;

import com.example.aopdihomework.aop.adapter.FailedCounterAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class FailedCounterAdapterImpl implements FailedCounterAdapter {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Integer getFailedCounter(String account) {
        HttpEntity<?> request = null;
        return restTemplate.exchange("api/failedCounter/GetFailedCount", HttpMethod.POST, request, Integer.class).getBody();
    }

    @Override
    public void addFailedCounter(String account) {
        HttpEntity<?> request = null;
        try {
            restTemplate.exchange("api/failedCounter/add", HttpMethod.POST, request, Integer.class).getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resetFailedCounter(String account) {
        HttpEntity<?> request = null;
        try {
            restTemplate.exchange("api/failedCounter/Reset", HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean isLocked(String account) {
        HttpEntity<?> request = null;
        try {
            return restTemplate.exchange("api/failedCounter/isLocked", HttpMethod.POST, request, Boolean.class).getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
            return false;
        }
    }
}