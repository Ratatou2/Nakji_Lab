package com.nakji.lab.dto.response;

import com.nakji.lab.common.dto.response.CommonResponse;

public class UpdateSongInfoResponse extends CommonResponse {
    // 기본 생성자
    public UpdateSongInfoResponse() {
        super(false, "");
    }


    // 파라미터 받는 생성자
    public UpdateSongInfoResponse(boolean isSuccess, String message) {
        super(isSuccess, message);
    }

    public UpdateSongInfoResponse(CommonResponse commonResponse) {
        super(commonResponse.isSuccess(), commonResponse.getMessage());
    }
}