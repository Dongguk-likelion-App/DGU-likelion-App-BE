package com.example.dgu_likelion_app.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_user_id", columnList = "userId", unique = true)
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String userId;

    // SHA-256 해시 저장(MVP)
    @Column(nullable = false, length = 128)
    private String password;

    @Column(nullable = false)
    private Integer generation;

    @Column(nullable = false, length = 30)
    private String track;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 50)
    private String major;

    // S3 업로드 후 URL 저장
    @Column(length = 500)
    private String confirmImg;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
