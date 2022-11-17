package com.example.aopdihomework.aop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AopController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/httpTest")
    @ResponseBody
    public ResponseEntity<String> getHttpRequest() {
        String remoteUrl = "https://randomuser.me/api/";
        return restTemplate.exchange(remoteUrl, HttpMethod.GET, null, String.class);
    }
}
