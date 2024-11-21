package com.nakji.lab.controller;

import com.nakji.lab.dto.request.YoutubeDownloadRequest;
import com.nakji.lab.dto.response.DownloadAndUpdateMp3FileResponse;
import com.nakji.lab.dto.response.UpdateSongInfoResponse;
import com.nakji.lab.dto.response.YoutubeDownloadResponse;
import com.nakji.lab.service.YoutubeService;

import com.nakji.lab.utill.YoutubeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/youtube")  // 웹 페이지가 아닌 기능의 결과값을 전달해줄 것이라서 /api 형태가 더 적합함
@RequiredArgsConstructor  // final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성한다
public class YoutubeController {
    private final String DEFAULT_LOG_CONTROLLER_NAME = "[YoutubeController]";
    private final String DEFAULT_LOG_SYSTEM_FAIL = "[SYSTEM][FAIL] ";

    private final YoutubeService youtubeService;

    @PostMapping("/download")
    public ResponseEntity<DownloadAndUpdateMp3FileResponse> downloadAndUpdateMp3File(@RequestBody YoutubeDownloadRequest youtubeDownloadRequest) {
        try {
            DownloadAndUpdateMp3FileResponse response = new DownloadAndUpdateMp3FileResponse();

            String url = youtubeDownloadRequest.getUrl();
            String artist = youtubeDownloadRequest.getArtist();
            String songTitle = youtubeDownloadRequest.getSongTitle();

            System.out.println("[SYSTEM][downloadAndUpdateMp3File][USER_INPUT] "
                    + url + " / "
                    + artist + " / "
                    + songTitle);

            // url의 유효성 검사
            if (YoutubeUtil.isValidUrl(youtubeDownloadRequest.getUrl())) {
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

                response = new DownloadAndUpdateMp3FileResponse(false, DEFAULT_LOG_SYSTEM_FAIL + youtubeDownloadResponse.getMessage(), artist, songTitle);

//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DEFAULT_LOG_SYSTEM_FAIL + youtubeDownloadResponse.getMessage());
                return ResponseEntity.ok(response);
            }

            // 노래 Info 업데이트
            UpdateSongInfoResponse updateSongInfoResponse = youtubeService.updateSongInfo();

            if (!updateSongInfoResponse.isSuccess()) {
                System.out.println(DEFAULT_LOG_CONTROLLER_NAME + "[downloadAndUpdateMp3File][Update Script][FAIL]" + updateSongInfoResponse.getMessage());
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DEFAULT_LOG_SYSTEM_FAIL + updateSongInfoResponse.getMessage());
                response = new DownloadAndUpdateMp3FileResponse(false, DEFAULT_LOG_SYSTEM_FAIL + updateSongInfoResponse.getMessage(), artist, songTitle);
                return ResponseEntity.ok(response);
            }

            response = new DownloadAndUpdateMp3FileResponse(true, "Success", artist, songTitle);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(DEFAULT_LOG_CONTROLLER_NAME + "[downloadAndUpdateMp3File][Exception] " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DEFAULT_LOG_SYSTEM_FAIL + "[Exception]" + e.getMessage());
            return ResponseEntity.ok(new DownloadAndUpdateMp3FileResponse(false, DEFAULT_LOG_SYSTEM_FAIL + "[Exception]" + e.getMessage()));
        }
    }
}
