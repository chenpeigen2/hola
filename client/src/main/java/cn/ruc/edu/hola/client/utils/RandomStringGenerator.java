package cn.ruc.edu.hola.client.utils;

import java.util.Random;
import java.util.UUID;

public class RandomStringGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static final String UID = "e51dce84-f2a4-43db-9b8f-311250ff0ba1";

    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateAESKey() {
        return generateRandomString(24);
    }

    public static void main(String[] args) {
        System.out.println(generateRandomUUID());
    }
}
