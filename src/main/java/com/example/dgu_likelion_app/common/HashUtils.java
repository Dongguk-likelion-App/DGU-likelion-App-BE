package com.example.dgu_likelion_app.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashUtils {

    // ✅ 별칭 메서드 추가
    public static String hash(String raw) {
        return sha256(raw);
    }

    public static String sha256(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }
}
