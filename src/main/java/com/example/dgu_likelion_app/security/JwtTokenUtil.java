package com.example.dgu_likelion_app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

    // 공통 빌더
    private static String buildToken(String userId, String key, long expireTimeMs) {
        long now = System.currentTimeMillis();

        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    // 액세스 토큰 생성
    public static String createAccessToken(String userId, String key, long expireTimeMs) {
        return buildToken(userId, key, expireTimeMs);
    }

    // 리프레시 토큰 생성
    public static String createRefreshToken(String userId, String key, long expireTimeMs) {
        return buildToken(userId, key, expireTimeMs);
    }

    // 토큰 만료 여부 확인
    public static boolean isExpired(String token, String secretKey) {
        Date expiredDate = extractClaims(token, secretKey).getExpiration();
        return expiredDate.before(new Date());
    }

    // 토큰에서 userId 꺼내기
    public static String getUserId(String token, String secretKey) {
        return extractClaims(token, secretKey).get("userId").toString();
    }

    // 내부적으로 claim 파싱
    private static Claims extractClaims(String token, String secretKey) {
        // jjwt 0.11.x 이상 스타일
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
