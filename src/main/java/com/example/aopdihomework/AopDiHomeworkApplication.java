package com.example.aopdihomework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class AopDiHomeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopDiHomeworkApplication.class, args);
    }

}
