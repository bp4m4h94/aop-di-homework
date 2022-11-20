package com.example.aopdihomework.aop.service;

import com.example.aopdihomework.aop.adapter.FailedCounterAdapter;
import com.example.aopdihomework.aop.adapter.OptAdopter;
import com.example.aopdihomework.aop.adapter.ProfilerRepo;
import com.example.aopdihomework.aop.adapter.impl.*;
import com.example.aopdihomework.aop.module.slack.SendMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class AuthenticationService {

    private final OptAdopter optAdopter;
    private final HashAdapterImpl hashAdapterImpl;
    private final ProfilerRepo passwordRepo;
    private final FailedCounterAdapter failedCounterAdapter;
    private final LoggerAdapter loggerAdapter;

    @Autowired
    private RestTemplate restTemplate;

    public AuthenticationService() {
        hashAdapterImpl = new HashAdapterImpl();
        passwordRepo = new ProfilerRepoImpl();
        failedCounterAdapter = new FailedCounterAdapterImpl();
        optAdopter = new OptAdopterImpl();
        loggerAdapter = new LoggerAdapterImpl();
    }


    // 上課示範
    @GetMapping("/db/customer/count")
    public boolean isValid(String account, String password, String otpCurrent) throws FailedTooManyTimesException {

        HttpEntity<?> request = null;
        boolean isLocked = failedCounterAdapter.isLocked(account);
        if (!isLocked) {
            passwordRepo.getPassword(account);
            String hashedPassword = hashAdapterImpl.getHash(password);
            String optAdopterOtp = optAdopter.getOtp(account);
            if (hashedPassword.equals(password) && optAdopterOtp.equals(otpCurrent)) {
                failedCounterAdapter.resetFailedCounter(account);
                return true;
            } else {
                Integer failCount = failedCounterAdapter.getFailedCounter(account);
                //  record log
                loggerAdapter.log("fail " + failCount + "times");
                HttpHeaders headers1 = new HttpHeaders();
                headers1.setContentType(MediaType.APPLICATION_JSON);
                SendMessageRequest req1 = new SendMessageRequest();
                req1.setText("Hello, notify slack");
                HttpEntity<SendMessageRequest> request2 = new HttpEntity<>(req1, headers1);
                failedCounterAdapter.addFailedCounter(account);
                return false;
            }
        } else {
            throw new FailedTooManyTimesException();
        }
    }
}


