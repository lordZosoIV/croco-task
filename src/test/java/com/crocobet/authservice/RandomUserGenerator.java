package com.crocobet.authservice;

import java.util.Random;

public class RandomUserGenerator {

    private static final String[] EMAIL_DOMAINS = {"gmail.com", "yahoo.com", "hotmail.com", "example.com"};
    private static final Random random = new Random();

    public static String generateRandomName() {
        return generateRandomString(6);
    }

    public static String generateRandomEmail() {
        String randomName = generateRandomString(10);
        String randomDomain = EMAIL_DOMAINS[random.nextInt(EMAIL_DOMAINS.length)];
        return randomName.toLowerCase() + "@" + randomDomain;
    }

    public static String generateRandomPassword() {
        return generateRandomString(8);
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }

        return randomString.toString();
    }


}