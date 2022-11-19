package com.example.aopdihomework.aop.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashAdapter {
    public HashAdapter() {
    }

    String getHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
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