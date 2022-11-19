package com.example.aopdihomework.aop.api;

import com.example.aopdihomework.aop.module.slack.SendMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ClassTestController {

    private final OptAdopter optAdopter;
    private final HashAdapter hashAdapter;
    private final passwordRepo passwordRepo;
    private final FailedCounterAdapter failedCounterAdapter;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.api.url}")
    private String randomUserUrl;

    @Value("${slack.api.postMessage.url}")
    private String slackUrl;

    @Value("${slack.api.param.channelId}")
    private String channelId;

    @Value("${slack.api.botToken}")
    private String botToken;

    public ClassTestController() {
        hashAdapter = new HashAdapter();
        passwordRepo = new passwordRepo();
        failedCounterAdapter = new FailedCounterAdapter();
        optAdopter = new OptAdopter();
    }


    // 上課示範
    @GetMapping("/db/customer/count")
    public boolean isValid(String accountId, String password, String otpCurrent) throws Exception {

        HttpEntity<?> request = null;
        boolean isLocked = restTemplate.exchange("api/failedCounter/isLocked",HttpMethod.POST, request, Boolean.class).getBody();
        if (isLocked) {
            passwordRepo.getPassword();
            String hashedPassword = hashAdapter.getHash(password);
            String otpformhttp = optAdopter.getOtp();
            if (hashedPassword.equals(password) && otpformhttp.equals(otpCurrent)) {
                failedCounterAdapter.resetFailedCounter(request);
                return true;
            } else {
                Integer failCount = failedCounterAdapter.getFailedCounter(request);
                //  record log
                log.info("fail times:" + failCount);
                HttpHeaders headers1 = new HttpHeaders();
                headers1.setContentType(MediaType.APPLICATION_JSON);
                SendMessageRequest req1 = new SendMessageRequest();
                req1.setText("Hello, notify slack");
                HttpEntity<SendMessageRequest> request2 = new HttpEntity<>(req1, headers1);
                failedCounterAdapter.addFailedCounter(request2);
                return false;
            }
        } else {
            throw new Exception();
        }
    }
}
