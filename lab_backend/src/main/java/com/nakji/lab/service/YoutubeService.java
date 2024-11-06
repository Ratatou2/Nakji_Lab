package com.nakji.lab.service;

import com.nakji.lab.dto.request.YoutubeDownloadRequest;
import com.nakji.lab.dto.response.UpdateSongInfoResponse;
import com.nakji.lab.dto.response.YoutubeDownloadResponse;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Service
public class YoutubeService {
    public YoutubeDownloadResponse youtubeDownload(YoutubeDownloadRequest youtubeDownloadRequest) {
        try {
            // 결과
            boolean isSuccess;
            String message;

            // 필요 변수
            int exitCode;
            BufferedReader bufferedReader;
            StringBuilder output;
            String line;
            ProcessBuilder processBuilder;
            Process process;


            // input 여백 제거
            String url = youtubeDownloadRequest.url().trim();
            String singer = youtubeDownloadRequest.singer().trim();
            String songName = youtubeDownloadRequest.songName().trim();

            // 로그
            System.out.println("[SYSTEM][YoutubeService][youtubeDownload]" + "\n"
                    + "Singer : " + singer + "\n"
                    + "Song Name : " + songName + "\n"
                    + "URL : " + url);

            // Python 스크립트 및 ffmpeg 경로
            String downloadScript = new File("src/main/resources/scripts/youtubeDownload.py").getAbsolutePath();
            String ffmpegPath = new File("src/main/resources/ffmpeg/ffmpeg.exe").getAbsolutePath();

            // ProcessBuilder 생성 (쉘이나 CMD로 실행 가능)
            processBuilder = new ProcessBuilder("python", downloadScript, url, "--ffmpeg-location", ffmpegPath, singer, songName);
            processBuilder.redirectErrorStream(true);
            processBuilder.environment().put("PYTHONIOENCODING", "utf-8"); // 환경 변수로 UTF-8 인코딩 설정

            process = processBuilder.start();

            // 프로세스의 출력 결과 읽기
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line); // 로그를 콘솔에 출력
                output.append(line).append("\n");
            }

            // 프로세스 종료 코드 확인
            exitCode = process.waitFor();
            if (exitCode == 0) {
                isSuccess = true;
                message = "[SUCCESS][" + singer + " - " + songName + "] Download successful";
            } else {
                isSuccess = false;
                message = "[FAIL][" + singer + " - " + songName + "] Error during download!! " + output;
            }

            System.out.println(message);

            return new YoutubeDownloadResponse(isSuccess, message);

        } catch (Exception e) {
            System.err.println("[SYSTEM][Exception][YoutubeService][youtubeDownload] " + e.getMessage());
            e.printStackTrace();
            return new YoutubeDownloadResponse(false, "[SYSTEM][Exception][YoutubeService][youtubeDownload] " + e.getMessage());
        }
    }

    public UpdateSongInfoResponse updateSongInfo() {
        try {
            // 결과
            boolean isSuccess;
            String message;

            // 필요 변수
            int exitCode;
            BufferedReader bufferedReader;
            StringBuilder output;
            String line;
            ProcessBuilder processBuilder;
            Process process;

            // 앨범 info 업데이트 part
            String updateScript = new File("src/main/resources/scripts/UpdateEveryMp3FileTag.py").getAbsolutePath();
            processBuilder = new ProcessBuilder("python", updateScript);

            process = processBuilder.start();

            // 프로세스의 출력 결과 읽기
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line); // 로그를 콘솔에 출력
                output.append(line).append("\n");
            }

            // 프로세스 종료 코드 확인
            exitCode = process.waitFor();
            if (exitCode == 0) {
                isSuccess = true;
                message = "[SUCCESS] Update Info Successful";
            } else {
                isSuccess = false;
                message = "[FAIL] Error During Update!! " + output;
            }

            return new UpdateSongInfoResponse(isSuccess, message);
        } catch (Exception e) {
            System.err.println("[SYSTEM][Exception][YoutubeService][youtubeDownload] " + e.getMessage());
            e.printStackTrace();
            return new UpdateSongInfoResponse(false, "[SYSTEM][Exception][YoutubeService][youtubeDownload] " + e.getMessage());
        }
    }
}
