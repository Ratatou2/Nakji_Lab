package com.nakji.lab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/youtube")
public class YoutubeController {

    @PostMapping("/download")
    public ResponseEntity<String> downloadAudio(@RequestParam String url, @RequestParam String singer, @RequestParam String songName) {
        try {
            // 로그
            System.out.println("[SYSTEM][YoutubeController][downloadAudio]" + "\n"
                    + "URL : " + url + "\n"
                    + "Singer : " + singer + "\n"
                    + "Song Name : " + songName);

            // Python 스크립트 및 ffmpeg 경로
            String scriptPath = new File("src/main/resources/scripts/youtubeDownload.py").getAbsolutePath();
            String ffmpegPath = new File("src/main/resources/ffmpeg/ffmpeg.exe").getAbsolutePath();

            // input 여백 제거
            url = url.trim();
            singer = singer.trim();
            songName = songName.trim();

            // ProcessBuilder 생성
            ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath, url, "--ffmpeg-location", ffmpegPath, singer, songName);
            processBuilder.redirectErrorStream(true);
            processBuilder.environment().put("PYTHONIOENCODING", "utf-8"); // 환경 변수로 UTF-8 인코딩 설정

            Process process = processBuilder.start();

            // 프로세스의 출력 결과 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // 로그를 콘솔에 출력
                output.append(line).append("\n");
            }

            // 프로세스 종료 코드 확인
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Download successful");
                return ResponseEntity.ok("Download successful:\n" + output);
            } else {
                System.err.println("Error during download:\n" + output);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during download:\n" + output);
            }
        } catch (Exception e) {
            System.err.println("Exception during script execution: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error executing script:\n" + e.getMessage());
        }
    }
}
