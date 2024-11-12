package com.nakji.lab.dto.response;

import com.nakji.lab.common.dto.response.CommonResponse;

public class YoutubeDownloadResponse extends CommonResponse {
    // 기본 생성자
    public YoutubeDownloadResponse() {
        super(false, "");  // 기본 값 설정
    }


    // 파라미터를 받는 생성자
    public YoutubeDownloadResponse(boolean isSuccess, String message) {
        super(isSuccess, message);
    }

    public YoutubeDownloadResponse(CommonResponse commonResponse) {
        super(commonResponse.isSuccess(), commonResponse.getMessage());
    }
}