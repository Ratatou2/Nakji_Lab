package com.nakji.lab.dto.response;

import com.nakji.lab.common.dto.response.CommonResponse;

import lombok.Getter;

@Getter  // 자동으로 Getter 메서드 생성
public class UpdateSongInfoResponse extends CommonResponse {
    public UpdateSongInfoResponse() {
        super(false, "");
    }

    public UpdateSongInfoResponse(boolean isSuccess, String message) {
        super(isSuccess, message);
    }
}