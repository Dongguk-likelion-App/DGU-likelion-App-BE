package com.example.dgu_likelion_app.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {

    @Value("${jwt.access-secret}")
    private String accessSecret;

    @Value("${jwt.access-expiration-ms}")
    private long accessExpirationMs;

    @Value("${jwt.refresh-secret}")
    private String refreshSecret;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    public String getAccessSecret() {
        return accessSecret;
    }

    public long getAccessExpirationMs() {
        return accessExpirationMs;
    }

    public String getRefreshSecret() {
        return refreshSecret;
    }

    public long getRefreshExpirationMs() {
        return refreshExpirationMs;
    }
}

