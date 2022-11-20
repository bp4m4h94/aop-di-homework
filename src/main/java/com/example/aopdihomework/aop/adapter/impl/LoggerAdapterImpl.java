package com.example.aopdihomework.aop.adapter.impl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerAdapterImpl implements LoggerAdapter {

    public void log(String failCount) {
        log.info(failCount);
    }
}