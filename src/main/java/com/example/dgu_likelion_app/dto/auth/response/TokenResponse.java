package com.example.dgu_likelion_app.dto.auth.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {}

