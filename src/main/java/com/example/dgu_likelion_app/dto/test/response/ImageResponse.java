package com.example.dgu_likelion_app.dto.test.response;

import com.example.dgu_likelion_app.domain.test.Image;
import lombok.Getter;

@Getter
public class ImageResponse {
    private final String imageUrl;

    public ImageResponse(Image image) {
        this.imageUrl = image.getImageUrl();
    }

}