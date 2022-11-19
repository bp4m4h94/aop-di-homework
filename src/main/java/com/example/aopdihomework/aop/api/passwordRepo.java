package com.example.aopdihomework.aop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class passwordRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    void getPassword() {
        String passwordFromDB = jdbcTemplate.queryForObject(
                "SELECT FIRST_NAME FROM DEQUE.CUSTOMER", String.class);
    }
}