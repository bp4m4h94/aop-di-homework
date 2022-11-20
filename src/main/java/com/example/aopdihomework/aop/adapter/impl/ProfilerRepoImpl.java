package com.example.aopdihomework.aop.adapter.impl;

import com.example.aopdihomework.aop.adapter.ProfilerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class ProfilerRepoImpl implements ProfilerRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getPassword(String account) {
        return  jdbcTemplate.queryForObject(
                "SELECT PASSWORD FROM DEQUE.ACCOUNT WHERE ACCOUNT = ? ", String.class, account);
    }
}