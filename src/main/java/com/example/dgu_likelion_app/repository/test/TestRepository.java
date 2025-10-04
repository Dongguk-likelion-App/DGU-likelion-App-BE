package com.example.dgu_likelion_app.repository.test;


import com.example.dgu_likelion_app.domain.test.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository  extends JpaRepository<Test, Long> {
}
