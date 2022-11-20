package com.example.aopdihomework.aop.adapter;

import org.springframework.http.HttpEntity;

public interface FailedCounterAdapter {
    Integer getFailedCounter(String account);

    void addFailedCounter(String account);

    void resetFailedCounter(String account);

    Boolean isLocked(String account);
}
