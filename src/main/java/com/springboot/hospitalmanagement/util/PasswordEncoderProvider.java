package com.springboot.hospitalmanagement.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderProvider {
    public static String passwordEncoding(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodepass = passwordEncoder.encode(password);
        return encodepass;
    }
}
