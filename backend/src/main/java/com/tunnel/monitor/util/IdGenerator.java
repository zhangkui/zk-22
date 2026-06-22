package com.tunnel.monitor.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class IdGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private IdGenerator() {
    }

    public static String generate(String prefix) {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return prefix + timestamp + uuid;
    }

    public static String generateShort(String prefix) {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        return prefix + timestamp;
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public static long snowflake() {
        return System.currentTimeMillis();
    }
}
