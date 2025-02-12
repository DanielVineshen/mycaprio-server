package org.everowl.shared.service.util;

import java.util.Random;

public class UniqueIdGenerator {
    public static String generateMyCaprioIdWithTimestamp() {
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        long timestamp = System.currentTimeMillis() % 1000000; // Last 6 digits of timestamp

        return String.format("MYC%06d", (randomNumber + timestamp) % 1000000);
    }

    public static String generateSmsCode() {
        Random random = new Random();
        // Generate a number between 100000 and 999999
        int code = random.nextInt(900000) + 100000;
        return String.format("%06d", code);
    }
}