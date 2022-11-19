package com.example.aopdihomework.aop.api;

import com.example.aopdihomework.aop.module.slack.SendMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;

@Slf4j
public class ClassTestController {

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

    // 上課示範
    @GetMapping("/db/customer/count")
    public boolean isValid(String accountId, String password, String otpCurrent) throws Exception {

        HttpEntity<?> request = null;
        boolean isLocked = restTemplate.exchange("api/failedCounter/isLocked",HttpMethod.POST, request, Boolean.class).getBody();
        if (isLocked) {
            String passwordFromDB = jdbcTemplate.queryForObject(
                    "SELECT FIRST_NAME FROM DEQUE.CUSTOMER", String.class);
            return "How many rows in Table \"CUSTOMER\" : " + result;

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] data = md.digest(password.getBytes());
            StringBuilder unhashPw = new StringBuilder();
            String hashedPassword = "";
            for(int i=0;i<data.length;i++)
            {
                unhashPw.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
            }
            System.out.println(unhashPw);
            hashedPassword = unhashPw.toString();


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth("botToken");
            SendMessageRequest req = new SendMessageRequest();
            HttpEntity<SendMessageRequest> request1 = new HttpEntity<>(req, headers);
            ResponseEntity<String> otpformhttp = restTemplate.exchange("www.otp.com", HttpMethod.POST, request1, String.class);

            if (hashedPassword == password && otpformhttp.toString() == otpCurrent) {
                restTemplate.exchange("api/failedCounter/Reset",HttpMethod.POST, request, String.class);
            } else {
                ResponseEntity<Integer> failCount = restTemplate.exchange("api/failedCounter/GetFailedCount", HttpMethod.POST, request, Integer.class);
                //  record log
                log.info("fail times:" + failCount.getBody());
                HttpHeaders headers1 = new HttpHeaders();
                headers1.setContentType(MediaType.APPLICATION_JSON);
                SendMessageRequest req1 = new SendMessageRequest();
                req1.setText("Hello, notify slack");
                HttpEntity<SendMessageRequest> request2 = new HttpEntity<>(req1, headers1);
                restTemplate.exchange(slackUrl, HttpMethod.POST, request2, String.class);

            }
        } else {
            throw new Exception();
        }

    }


}
