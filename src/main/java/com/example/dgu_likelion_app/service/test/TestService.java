package com.example.dgu_likelion_app.service.test;

import com.example.dgu_likelion_app.domain.test.Test;
import com.example.dgu_likelion_app.dto.test.request.TestCreateRequest;
import com.example.dgu_likelion_app.dto.test.response.TestResponse;
import com.example.dgu_likelion_app.repository.test.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TestService {

    private final TestRepository testRepository;

    @Transactional
    public TestResponse saveTestData(TestCreateRequest request) {
        Test savedTest = testRepository.save(new Test(request.getName()));
        return new TestResponse(savedTest.getName());
    }
}
