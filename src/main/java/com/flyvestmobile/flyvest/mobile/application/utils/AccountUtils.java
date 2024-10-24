package com.flyvestmobile.flyvest.mobile.application.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

@Component
public class AccountUtils {
    public static String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            password.append(random.nextInt(10));
        }

        String chars = "abcdefghijklmnopqrst@#$%&";
        for (int i = 0; i < 3; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

}
