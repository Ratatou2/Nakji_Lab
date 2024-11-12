package com.nakji.lab.common.dto.response;

import lombok.Getter;

@Getter
public class CommonResponse {
    private final boolean isSuccess;
    private final String message;

    // 파라미터를 받는 생성자 추가
    public CommonResponse(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    @Override
    public String toString() {
        return "[SYSTEM][" + (isSuccess ? "성공" : "실패") + "] | " + message + "]";
    }
}
