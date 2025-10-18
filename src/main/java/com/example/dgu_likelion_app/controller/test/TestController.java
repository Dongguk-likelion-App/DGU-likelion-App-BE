package com.example.dgu_likelion_app.controller.test;

import com.example.dgu_likelion_app.dto.test.request.TestCreateRequest;
import com.example.dgu_likelion_app.dto.test.response.ImageResponse;
import com.example.dgu_likelion_app.dto.test.response.TestResponse;
import com.example.dgu_likelion_app.service.test.ImageService;
import com.example.dgu_likelion_app.service.test.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final TestService testService;
    private final ImageService imageService;

    @GetMapping("/")
    public String home() {
        return "동멋앱 서버가 정상적으로 실행중입니다!";
    }

    @PostMapping("/test")
    public TestResponse saveTestData(@RequestBody TestCreateRequest request) {
        return testService.saveTestData(request);
    }

    @PostMapping("/api/images")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            ImageResponse imageResponse = imageService.uploadImage(file);
            return ResponseEntity.ok(imageResponse);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/api/images/{imageId}")
    public ResponseEntity<ImageResponse> getImage(@PathVariable Long imageId) {
        try {
            ImageResponse imageResponse = imageService.getImage(imageId);
            return ResponseEntity.ok(imageResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}