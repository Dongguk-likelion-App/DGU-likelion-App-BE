package com.example.dgu_likelion_app.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String password;

    @NotNull
    private Integer generation;

    @NotBlank
    private String track;

    @NotBlank
    private String name;

    @NotBlank
    private String major;
}
