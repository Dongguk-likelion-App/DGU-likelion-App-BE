package com.example.dgu_likelion_app.controller.auth;

import com.example.dgu_likelion_app.dto.user.SignUpRequest;
import com.example.dgu_likelion_app.dto.user.UserResponse;
import com.example.dgu_likelion_app.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponse signUp(
            @Valid @ModelAttribute SignUpRequest request,
            @RequestParam(value = "confirmImg", required = false) MultipartFile confirmImg
    ) {
        return userService.signUp(request, confirmImg);
    }
}
