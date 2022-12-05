package com.springboot.hospitalmanagement.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPasswordGenerator {
    public static String passwordGenerator() {
        String password = UUID.randomUUID().toString();
        long pass = ThreadLocalRandom.current().nextLong();
        return password.substring(0, 7);
    }

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pass = passwordEncoder.encode("admin");
        System.out.println(pass);
    }

}
