package com.example.dgu_likelion_app.repository.user;

import com.example.dgu_likelion_app.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUserId(String userId);
}
