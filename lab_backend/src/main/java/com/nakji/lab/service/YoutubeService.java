package com.nakji.lab.service;

import com.nakji.lab.dto.request.YoutubeDownloadRequest;
import com.nakji.lab.dto.response.UpdateSongInfoResponse;
import com.nakji.lab.dto.response.YoutubeDownloadResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class YoutubeService {
    @Value("${custom.resource.path}")
    private String externalResourcePath;

    public boolean isLinux() {
        // 운영체제 정보 가져오기
        String os = System.getProperty("os.name").toLowerCase();

        System.out.println("현재 OS : " + os);

        // OS에 따라 다른 메시지 출력
        if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return true;
        } else {
            return false;
        }
    }

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

            // 리눅스 환경이면 경로 변경하자
            boolean isLinux = isLinux();
            if (isLinux) {
                externalResourcePath = "/app/resources-external";
            }

            // Python 스크립트 및 ffmpeg 경로
            String downloadScript = Paths.get(externalResourcePath, "scripts/youtubeDownload.py").toString();

            String ffmpegPath = isLinux ? "ffmpeg_linux" : "ffmpeg/ffmpeg.exe";

            System.out.println("운영 환경에 따른 ffmpeg 경로 : " + ffmpegPath);
            ffmpegPath = Paths.get(externalResourcePath, ffmpegPath).toString();  // Linux에서는 .exe가 아닌 ffmpeg 통째로 실행됨
//            String ffmpegPath = Paths.get(externalResourcePath, "ffmpeg/ffmpeg.exe").toString();

            String mp3Path = Paths.get(externalResourcePath, "mp3file").toString();
            String mp3PathTemp = Paths.get(externalResourcePath + "/mp3file").toString();

            System.out.println("mp3Path 경로 : " + mp3Path);
            System.out.println("mp3PathTemp 경로 : " + mp3PathTemp);

            // ProcessBuilder 생성 (쉘이나 CMD로 실행 가능)
            processBuilder = new ProcessBuilder("python", downloadScript, url, "--ffmpeg-location", ffmpegPath, singer, songName, mp3Path);
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
            String updateScript = String.valueOf(Paths.get(externalResourcePath, "scripts/UpdateEveryMp3FileTag.py"));
            String mp3Path = String.valueOf(Paths.get(externalResourcePath + "/mp3file"));

            System.out.println("경로 : " + mp3Path);
            processBuilder = new ProcessBuilder("python", updateScript, "--mp3path", mp3Path);

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
