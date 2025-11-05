package com.example.dgu_likelion_app.domain.auth;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 누가 쓰는 토큰인지
    @Column(nullable = false, length = 50)
    private String userId;

    // 실제 토큰 문자열 (JWT)
    @Column(nullable = false, length = 512, unique = true)
    private String token;

    // 만료 시간
    @Column(nullable = false)
    private Instant expiresAt;

    // 강제 로그아웃 등으로 무효화했는지 여부
    @Column(nullable = false)
    private boolean revoked;
}
