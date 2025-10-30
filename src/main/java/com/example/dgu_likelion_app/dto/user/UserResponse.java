package com.example.dgu_likelion_app.dto.user;

import java.time.LocalDateTime;

public record UserResponse(
        Integer id,
        String userId,
        Integer generation,
        String track,
        String name,
        String major,
        String confirmImg,
        LocalDateTime createdAt
) {}
