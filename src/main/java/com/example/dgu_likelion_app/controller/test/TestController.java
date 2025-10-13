package com.example.dgu_likelion_app.controller.test;

import com.example.dgu_likelion_app.dto.test.request.TestCreateRequest;
import com.example.dgu_likelion_app.dto.test.response.TestResponse;
import com.example.dgu_likelion_app.service.test.S3Service; // S3Service 임포트
import com.example.dgu_likelion_app.service.test.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final TestService testService;
    private final S3Service s3Service; // S3Service 주입

    @GetMapping("/")
    public String home() {
        return "동멋앱 서버가 정상적으로 실행중입니다!";
    }

    @PostMapping("/test")
    public TestResponse saveTestData(@RequestBody TestCreateRequest request) {
        return testService.saveTestData(request);
    }

    @PostMapping("/api/upload/image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = s3Service.uploadFile(file); // S3Service의 메서드 호출
            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", imageUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}