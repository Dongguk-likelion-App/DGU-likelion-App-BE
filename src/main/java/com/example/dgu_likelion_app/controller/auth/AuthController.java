package com.example.dgu_likelion_app.controller.auth;

import com.example.dgu_likelion_app.domain.user.User;
import com.example.dgu_likelion_app.domain.auth.RefreshToken;
import com.example.dgu_likelion_app.dto.auth.request.RefreshRequest;
import com.example.dgu_likelion_app.dto.auth.response.TokenResponse;
import com.example.dgu_likelion_app.dto.auth.request.LoginRequest;
import com.example.dgu_likelion_app.dto.user.SignUpRequest;
import com.example.dgu_likelion_app.dto.user.UserResponse;
import com.example.dgu_likelion_app.security.JwtProperties;
import com.example.dgu_likelion_app.security.JwtTokenUtil;
import com.example.dgu_likelion_app.service.auth.RefreshTokenService;
import com.example.dgu_likelion_app.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;

    // 회원가입 (기존 그대로)
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponse signUp(
            @Valid @ModelAttribute SignUpRequest request,
            @RequestParam(value = "confirmImg", required = false) MultipartFile confirmImg
    ) {
        return userService.signUp(request, confirmImg);
    }

    // 로그인 -> access + refresh 발급
    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest request) {

        User loginUser = userService.login(request);

        if (loginUser == null) {
            // 401 Unauthorized
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        // 액세스 토큰
        String accessToken = JwtTokenUtil.createAccessToken(
                loginUser.getUserId(),
                jwtProperties.getAccessSecret(),
                jwtProperties.getAccessExpirationMs()
        );

        // 리프레시 토큰
        String refreshToken = JwtTokenUtil.createRefreshToken(
                loginUser.getUserId(),
                jwtProperties.getRefreshSecret(),
                jwtProperties.getRefreshExpirationMs()
        );

        // 리프레시 토큰 저장
        Instant refreshExpiresAt = Instant.now().plusMillis(jwtProperties.getRefreshExpirationMs());
        refreshTokenService.save(loginUser.getUserId(), refreshToken, refreshExpiresAt);

        // 프론트로 access / refresh 둘 다
        return new TokenResponse(accessToken, refreshToken);
    }

    // access 토큰 만료 시 새 access 토큰 재발급
    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody @Valid RefreshRequest req) {

        // 1) 리프레시 토큰 유효성 검사
        RefreshToken stored = refreshTokenService.validateRefreshTokenOrThrow(req.refreshToken());

        // 2) 리프레시 토큰 자체의 JWT 서명/만료도 확인
        if (JwtTokenUtil.isExpired(req.refreshToken(), jwtProperties.getRefreshSecret())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰 만료");
        }

        String userId = JwtTokenUtil.getUserId(req.refreshToken(), jwtProperties.getRefreshSecret());

        // 3) 새로운 access 토큰 발급
        String newAccessToken = JwtTokenUtil.createAccessToken(
                userId,
                jwtProperties.getAccessSecret(),
                jwtProperties.getAccessExpirationMs()
        );

        String sameRefreshToken = stored.getToken();

        return new TokenResponse(newAccessToken, sameRefreshToken);
    }

    // 로그인된 사용자 정보 확인 (Authorization: Bearer <accessToken>)
    @GetMapping("/me")
    public String me(Authentication authentication) {
        // JwtAuthFilter가 SecurityContext에 넣어준 principal == userId
        return "you are " + authentication.getName();
    }

    /** 로그인된 사용자의 모든 Refresh 토큰 폐기 = 전체 로그아웃 */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        if (authentication == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String userId = authentication.getName();
        refreshTokenService.revokeAllByUserId(userId);
        return ResponseEntity.noContent().build(); // 204
    }

    // 본인이 가진 특정 Refresh 토큰 한 개만 폐기
    @PostMapping("/logout/token")
    public ResponseEntity<Void> logoutByToken(Authentication authentication,
                                              @RequestBody @Valid RefreshRequest req) {
        if (authentication == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String userId = authentication.getName();
        refreshTokenService.revokeByTokenForUser(userId, req.refreshToken());
        return ResponseEntity.noContent().build(); // 204
    }
}
