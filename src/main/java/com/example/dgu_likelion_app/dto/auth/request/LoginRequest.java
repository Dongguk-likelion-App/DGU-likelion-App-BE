package com.example.dgu_likelion_app.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class LoginRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String password;
}
