package com.example.dgu_likelion_app.repository.test;

import com.example.dgu_likelion_app.domain.test.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}