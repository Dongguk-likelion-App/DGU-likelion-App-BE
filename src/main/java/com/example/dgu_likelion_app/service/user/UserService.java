package com.example.dgu_likelion_app.service.user;

import com.example.dgu_likelion_app.common.HashUtils;
import com.example.dgu_likelion_app.domain.user.User;
import com.example.dgu_likelion_app.dto.user.SignUpRequest;
import com.example.dgu_likelion_app.dto.user.UserResponse;
import com.example.dgu_likelion_app.repository.user.UserRepository;
import com.example.dgu_likelion_app.service.image.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageUploadService imageUploadService;

    @Transactional
    public UserResponse signUp(SignUpRequest req, MultipartFile confirmImg) {
        // 중복 userId 검사
        if (userRepository.existsByUserId(req.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 해시
        String hashedPassword = HashUtils.sha256(req.getPassword());

        // 이미지 업로드 (있을 경우)
        String confirmImgUrl = null;
        if (confirmImg != null && !confirmImg.isEmpty()) {
            confirmImgUrl = imageUploadService.uploadAndGetUrl(confirmImg);
        }

        // 저장
        User saved = userRepository.save(
                User.builder()
                        .userId(req.getUserId())
                        .password(hashedPassword)
                        .generation(req.getGeneration())
                        .track(req.getTrack())
                        .name(req.getName())
                        .major(req.getMajor())
                        .confirmImg(confirmImgUrl)
                        .build()
        );

        // 응답
        return new UserResponse(
                saved.getId(),
                saved.getUserId(),
                saved.getGeneration(),
                saved.getTrack(),
                saved.getName(),
                saved.getMajor(),
                saved.getConfirmImg(),
                saved.getCreatedAt()
        );
    }
}
