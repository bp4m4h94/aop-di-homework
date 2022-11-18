package com.example.aopdihomework.aop.module.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SendMessageRequest {

    @JsonProperty("channel")
    private String channelId;

    @JsonProperty("text")
    private String text;
}
