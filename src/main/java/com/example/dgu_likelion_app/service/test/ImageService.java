package com.example.dgu_likelion_app.service.test;

import com.example.dgu_likelion_app.domain.test.Image;
import com.example.dgu_likelion_app.dto.test.response.ImageResponse;
import com.example.dgu_likelion_app.repository.test.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    @Transactional
    public ImageResponse uploadImage(MultipartFile file) throws IOException {
        String imageUrl = s3Service.uploadFile(file);
        Image savedImage = imageRepository.save(new Image(imageUrl));
        return new ImageResponse(savedImage);
    }

    @Transactional(readOnly = true)
    public ImageResponse getImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));
        return new ImageResponse(image);
    }
}