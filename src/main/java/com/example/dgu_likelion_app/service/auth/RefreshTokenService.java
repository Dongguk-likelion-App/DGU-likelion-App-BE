// src/main/java/com/example/dgu_likelion_app/service/auth/RefreshTokenService.java
package com.example.dgu_likelion_app.service.auth;

import com.example.dgu_likelion_app.domain.auth.RefreshToken;
import com.example.dgu_likelion_app.repository.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(String userId, String refreshToken, Instant expiresAt) {
        RefreshToken entity = RefreshToken.builder()
                .userId(userId)
                .token(refreshToken)
                .expiresAt(expiresAt)
                .revoked(false)
                .build();
        refreshTokenRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public RefreshToken validateRefreshTokenOrThrow(String refreshToken) {
        RefreshToken rt = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리프레시 토큰"));
        if (rt.isRevoked()) throw new IllegalArgumentException("리프레시 토큰이 폐기되었습니다.");
        if (rt.getExpiresAt().isBefore(Instant.now())) throw new IllegalArgumentException("리프레시 토큰이 만료되었습니다.");
        return rt;
    }

    /** 현재 사용자(userId)의 모든 활성 Refresh 토큰 폐기 (권장 로그아웃 방식) */
    @Transactional
    public int revokeAllByUserId(String userId) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserIdAndRevokedFalse(userId);
        tokens.forEach(rt -> rt.setRevoked(true));
        return tokens.size();
    }

    /** 특정 Refresh 토큰만 폐기 */
    @Transactional
    public void revokeByTokenForUser(String userId, String token) {
        RefreshToken rt = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리프레시 토큰"));
        if (!rt.getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 사용자 소유의 리프레시 토큰이 아닙니다.");
        }
        if (!rt.isRevoked()) rt.setRevoked(true);
    }
}
