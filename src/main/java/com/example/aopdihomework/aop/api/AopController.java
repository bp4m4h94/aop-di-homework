package com.example.aopdihomework.aop.api;

import com.example.aopdihomework.aop.module.slack.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class AopController {

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


    @GetMapping("/httpTest")
    public ResponseEntity<String> getRandomUser() {
        return restTemplate.exchange(randomUserUrl, HttpMethod.GET, null, String.class);
    }

    @GetMapping("/slack/sendMessage")
    public ResponseEntity<String> sendMessage() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(botToken);
        SendMessageRequest req = new SendMessageRequest();
        req.setChannelId(channelId);
        req.setText("Hello, Ryan Tsai:tada:");
        HttpEntity<SendMessageRequest> request = new HttpEntity<>(req, headers);
        return restTemplate.exchange(slackUrl, HttpMethod.POST, request, String.class);

    }

}
