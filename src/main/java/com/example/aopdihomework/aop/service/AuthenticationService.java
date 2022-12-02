package com.example.aopdihomework.aop.service;

import com.example.aopdihomework.aop.adapter.*;
import com.example.aopdihomework.aop.adapter.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class AuthenticationService {

    private final OptAdopter _optAdopter;
    private final HashAdapter _hashAdapterImpl;
    private final ProfilerRepo _passwordRepo;
    private final FailedCounterAdapter _failedCounterAdapter;
    private final LoggerAdapter _loggerAdapter;
    private final NotificationAdapter _notificationAdapter;

    @Value("${slack.api.postMessage.url}")
    private String slackUrl;

    @Value("spring.datasource.password")
    private String userPwd;

    @Value("spring.datasource.username")
    private String userAcct;


    @Autowired
    private RestTemplate restTemplate;


    public AuthenticationService(HashAdapter hashAdapter, ProfilerRepo profilerRepo, FailedCounterAdapter failedCounterAdapter, OptAdopter optAdopter, LoggerAdapter loggerAdapter, NotificationAdapter notificationAdapter) {
        _hashAdapterImpl = hashAdapter;
        _passwordRepo = profilerRepo;
        _failedCounterAdapter = failedCounterAdapter;
        _optAdopter = optAdopter;
        _loggerAdapter = loggerAdapter;
        _notificationAdapter = notificationAdapter;
    }


    // 上課示範
    @GetMapping("/db/customer/count")
    public boolean isValid(String account, String password, String otpCurrent) throws FailedTooManyTimesException {

        boolean isLocked = _failedCounterAdapter.isLocked(account);
        if (!isLocked) {
            _passwordRepo.getPassword(account);
//            String hashedPassword = hashAdapterImpl.getHash(userPwd);
            String optAdopterOtp = _optAdopter.getOtp(account);
            if (userPwd.equals(password) && optAdopterOtp.equals(otpCurrent)) {
                _failedCounterAdapter.resetFailedCounter(account);
                return true;
            } else {
                Integer failCount = _failedCounterAdapter.getFailedCounter(account);
                //  record log
                _loggerAdapter.log("fail " + failCount + "times");
                _notificationAdapter.sendNotification(account, slackUrl);
                _failedCounterAdapter.addFailedCounter(account);
                return false;
            }
        } else {
            throw new FailedTooManyTimesException();
        }
    }


}


