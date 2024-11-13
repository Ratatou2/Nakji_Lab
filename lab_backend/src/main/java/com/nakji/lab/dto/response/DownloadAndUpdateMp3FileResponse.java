package com.nakji.lab.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class DownloadAndUpdateMp3FileResponse {
    private boolean isSuccess;
    private String message;
    private String artist;
    private String songTitle;

    public DownloadAndUpdateMp3FileResponse() {}

    public DownloadAndUpdateMp3FileResponse(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
