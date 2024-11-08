package com.nakji.lab.controller;

import com.nakji.lab.dto.request.YoutubeDownloadRequest;
import com.nakji.lab.dto.response.UpdateSongInfoResponse;
import com.nakji.lab.dto.response.YoutubeDownloadResponse;
import com.nakji.lab.service.YoutubeService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/youtube")  // 웹 페이지가 아닌 기능의 결과값을 전달해줄 것이라서 /api 형태가 더 적합함
@RequiredArgsConstructor  // final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성한다
public class YoutubeController {
    private final String DEFAULT_LOG_CONTROLLER_NAME = "[YoutubeController]";
    private final String DEFAULT_LOG_SYSTEM_FAIL = "[SYSTEM][FAIL] ";

    private final YoutubeService youtubeService;

    @PostMapping("/download")
    public ResponseEntity<String> downloadAndUpdateMp3File(@RequestParam String url, @RequestParam String singer, @RequestParam String songName) {
        try {
            // 노래 다운로드
            YoutubeDownloadRequest youtubeDownloadRequest = new YoutubeDownloadRequest(url, singer, songName);
            YoutubeDownloadResponse youtubeDownloadResponse = youtubeService.youtubeDownload(youtubeDownloadRequest);

            if (!youtubeDownloadResponse.isSuccess()) {
                System.out.println(DEFAULT_LOG_CONTROLLER_NAME + "[downloadAndUpdateMp3File][Download Script][FAIL] " + youtubeDownloadResponse.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DEFAULT_LOG_SYSTEM_FAIL + youtubeDownloadResponse.getMessage());
            }

            // 노래 Info 업데이트
            UpdateSongInfoResponse updateSongInfoResponse = youtubeService.updateSongInfo();

            if (!updateSongInfoResponse.isSuccess()) {
                System.out.println(DEFAULT_LOG_CONTROLLER_NAME + "[downloadAndUpdateMp3File][Update Script][FAIL]" + updateSongInfoResponse.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DEFAULT_LOG_SYSTEM_FAIL + updateSongInfoResponse.getMessage());
            }

            return ResponseEntity.ok("[SUCCESS] Download and update completed successfully.");
        } catch (Exception e) {
            System.err.println(DEFAULT_LOG_CONTROLLER_NAME + "[downloadAndUpdateMp3File][Exception] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DEFAULT_LOG_SYSTEM_FAIL + "[Exception]" + e.getMessage());
        }
    }
}
