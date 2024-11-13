package com.nakji.lab.controller;

import com.nakji.lab.config.WebConfig;
import com.nakji.lab.dto.request.YoutubeDownloadRequest;
import com.nakji.lab.dto.response.UpdateSongInfoResponse;
import com.nakji.lab.dto.response.YoutubeDownloadResponse;
import com.nakji.lab.service.YoutubeService;

import com.nakji.lab.utill.YoutubeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/youtube")  // 웹 페이지가 아닌 기능의 결과값을 전달해줄 것이라서 /api 형태가 더 적합함
@RequiredArgsConstructor  // final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성한다
public class YoutubeController {
    private final String DEFAULT_LOG_CONTROLLER_NAME = "[YoutubeController]";
    private final String DEFAULT_LOG_SYSTEM_FAIL = "[SYSTEM][FAIL] ";

    private final YoutubeService youtubeService;
    private final YoutubeUtil youtubeUtil;

    private final WebConfig webConfig;

    @PostMapping("/download")
    public ResponseEntity<String> downloadAndUpdateMp3File(@RequestBody YoutubeDownloadRequest youtubeDownloadRequest) {
        try {
            webConfig.getProperty("server");

            System.out.println("[SYSTEM][downloadAndUpdateMp3File][USER_INPUT] "
                    + youtubeDownloadRequest.getUrl() + " / "
                    + youtubeDownloadRequest.getArtist() + " / "
                    + youtubeDownloadRequest.getSongTitle());

            // url의 유효성 검사
            if (youtubeUtil.isValidUrl(youtubeDownloadRequest.getUrl())) {
                throw new IllegalArgumentException("Wrong URL.");
            }

            // 100자가 넘는 artist 이름 예외처리
            if (100 < youtubeDownloadRequest.getArtist().length()) {
                throw new IllegalArgumentException("Artist name is too long.");
            }

            // 혹시 모를 null 값 체크
            if (youtubeDownloadRequest.getUrl() == null || youtubeDownloadRequest.getUrl().isEmpty()) {
                throw new IllegalArgumentException("URL is required.");
            }

            // 노래 다운로드
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
