package com.example.aopdihomework.aop.adapter.impl;

import com.example.aopdihomework.aop.adapter.HashAdapter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashAdapterImpl implements HashAdapter {

    public String getHash(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] data = md.digest(password.getBytes());
        StringBuilder unhashPw = new StringBuilder();
        String hashedPassword = "";
        for (int i = 0; i < data.length; i++) {
            unhashPw.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
        }
        hashedPassword = unhashPw.toString();
        return hashedPassword;
    }
}