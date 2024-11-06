package com.nakji.lab.controller;

import com.nakji.lab.dto.request.YoutubeDownloadRequest;
import com.nakji.lab.dto.response.UpdateSongInfoResponse;
import com.nakji.lab.dto.response.YoutubeDownloadResponse;
import com.nakji.lab.service.YoutubeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/youtube")  // 웹 페이지가 아닌 기능의 결과값을 전달해줄 것이라서 /api 형태가 더 적합함
public class YoutubeController {
    private final YoutubeService youtubeService;

    // 서비스 의존성 주입 (Constructor Injection)
    @Autowired
    public YoutubeController(YoutubeService youTubeService) {
        this.youtubeService = youTubeService;
    }

    @PostMapping("/download")
    public ResponseEntity<String> downloadMp3File(@RequestParam String url, @RequestParam String singer, @RequestParam String songName) {
        try {

            // 노래 다운로드
            YoutubeDownloadRequest youtubeDownloadRequest = new YoutubeDownloadRequest(url, singer, songName);
            YoutubeDownloadResponse youtubeDownloadResponse = youtubeService.youtubeDownload(youtubeDownloadRequest);

            if (!youtubeDownloadResponse.isSuccess()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("[SYSTEM] Error executing script:\n" + youtubeDownloadResponse.getMessage());
            }

            // 노래 Info 업데이트
            UpdateSongInfoResponse updateSongInfoResponse = youtubeService.updateSongInfo();

            if (!updateSongInfoResponse.isSuccess()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("[SYSTEM] Error executing script:\n" + youtubeDownloadResponse.getMessage());
            }

            return ResponseEntity.ok("[SUCCESS] Download and update completed successfully.");
        } catch (Exception e) {
            System.err.println("[SYSTEM] Exception during script execution: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("[SYSTEM] Error executing script:\n" + e.getMessage());
        }
    }
}
