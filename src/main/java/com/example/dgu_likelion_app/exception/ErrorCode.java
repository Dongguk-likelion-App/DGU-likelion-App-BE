package com.example.dgu_likelion_app.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    DUPLICATE_USER_ID(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 실패"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청"),
    INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST, "허용되지 않는 값");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
    public HttpStatus getStatus() { return status; }
    public String getDefaultMessage() { return defaultMessage; }
}
