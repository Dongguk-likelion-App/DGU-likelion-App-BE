package com.example.dgu_likelion_app.service.image;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.region.static:ap-northeast-2}")
    private String region;

    public String uploadAndGetUrl(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        String ext = extractExt(file.getOriginalFilename());
        String key = buildKey(ext);

        try {
            // 버킷 퍼블릭 정책을 가정합니다. (CloudFront/프리사인 URL 전략이면 여기를 바꾸면 됩니다)
            s3Template.upload(bucket, key, file.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("S3 업로드 실패: " + e.getMessage(), e);
        }

        // 퍼블릭 URL 구성
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
    }

    private String extractExt(String original) {
        if (original == null || !original.contains(".")) return ".bin";
        return original.substring(original.lastIndexOf("."));
    }

    private String buildKey(String ext) {
        String ymd = LocalDate.now().toString(); // e.g., 2025-10-28
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return "images/" + ymd + "/" + uuid + ext.toLowerCase();
    }
}
