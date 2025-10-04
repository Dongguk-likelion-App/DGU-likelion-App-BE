package com.example.dgu_likelion_app.controller.test;

import com.example.dgu_likelion_app.dto.test.request.TestCreateRequest;
import com.example.dgu_likelion_app.dto.test.response.TestResponse;
import com.example.dgu_likelion_app.service.test.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final TestService testService;

    @GetMapping("/")
    public String home() {
        return "동멋앱 서버가 정상적으로 실행중입니다!";
    }
    @PostMapping("/test")
    public TestResponse saveTestData(@RequestBody TestCreateRequest request) {
        return testService.saveTestData(request);
    }


}
