package com.nakji.lab.service;

import com.nakji.lab.common.dto.response.CommonResponse;
import com.nakji.lab.dto.request.YoutubeDownloadRequest;
import com.nakji.lab.dto.response.UpdateSongInfoResponse;
import com.nakji.lab.dto.response.YoutubeDownloadResponse;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;


@Service
public class YoutubeService {
    private final String DEFAULT_LOG_EXCEPTION = "[EXCEPTION][YoutubeService]";  // YoutubeService Default 로그명
    private final boolean isLinux = isLinux();  // 중간에 사용환경이 변할리는 없으니까 인스턴스 생성할 때 고정한다
    private String externalResourcePath;

    /**
     * 현재 운영체제가 리눅스인지 체크
     * @return - 현 운영체제가 리눅스인지 여부
     */
    public boolean isLinux() {
        String osName = System.getProperty("os.name").toLowerCase();  // 운영체제 정보 가져오기
        return (osName.contains("nix") || osName.contains("nux") || osName.contains("mac"));
    }

    /**
     * OS 체크하여, OS에 맞는 externalResourcePath 설정
     * */
    @PostConstruct
    public void initExternalResourcePath() {
        externalResourcePath = isLinux ? "/app/resources-external" : "./resources-external";
        System.out.println("[SYSTEM][최초 리눅스 체크] " + externalResourcePath);
    }

    /**
     * 프로세스 실행 함수
     * @param processBuilder - 실행할 프로세스
     * @param executeFunctionName - 실행한 함수명
     * @return - 실행 결과
     */
    public CommonResponse executeProcess (ProcessBuilder processBuilder, String executeFunctionName) {
        try {
            // 프로세스 실행
            Process process = processBuilder.start();

            // 프로세스의 출력 결과 읽기
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();

            // 결과값 읽기
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line); // 로그를 콘솔에 출력
                output.append(line).append("\n");
            }

            // 프로세스 결과 체크
            boolean isSuccess;
            String message;
            int exitCode = process.waitFor();  // 프로세스 종료 코드 확인
            if (exitCode == 0) {
                isSuccess = true;
                message = "[PROCESS SUCCESS][";
            } else {
                isSuccess = false;
                message = "[PROCESS FAIL][";
            }

            message += executeFunctionName + "] " + output;

            return new CommonResponse(isSuccess, message);
        } catch (Exception e) {
            System.err.println(DEFAULT_LOG_EXCEPTION + "[executeProcess] " + e.getMessage());
            return new CommonResponse(false, DEFAULT_LOG_EXCEPTION + "[executeProcess] " + e.getMessage());
        }
    }

    /**
     * 유튜브 링크와 가수, 노래 제목을 input으로 받는 다운로드 함수
     * @param youtubeDownloadRequest - 다운로드에 필요한 Info (유튜브 링크, 가수, 노래제목)
     * @return - 유튜브 다운로드 결과
     */
    public YoutubeDownloadResponse youtubeDownload(YoutubeDownloadRequest youtubeDownloadRequest) {
        try {
            // input 여백 제거
            String url = youtubeDownloadRequest.url().trim();
            String singer = youtubeDownloadRequest.singer().trim();
            String songName = youtubeDownloadRequest.songName().trim();

            // 리눅스 환경 여부에 따라 스크립트 세팅 (리눅스 환경이면 ffmpeg 경로 세팅이 필요없음)
            String downloadScriptDir = isLinux ? "scripts/youtubeDownload_linux.py" : "scripts/youtubeDownload.py";

            // Python 스크립트 및 ffmpeg 경로
            String downloadScript = String.valueOf(Paths.get(externalResourcePath, downloadScriptDir));
            String ffmpegPath = String.valueOf(Paths.get(externalResourcePath, "ffmpeg/ffmpeg.exe"));
            String mp3Path = String.valueOf(Paths.get(externalResourcePath, "mp3file"));

            ProcessBuilder processBuilder = isLinux ?
                    new ProcessBuilder("python", downloadScript, url, singer, songName, mp3Path) :
                    new ProcessBuilder("python", downloadScript, url, "--ffmpeg-location", ffmpegPath, singer, songName, mp3Path);

            processBuilder.redirectErrorStream(true);  // 오류 메시지와 일반 출력 메시지를 모두 표준 출력 스트림으로 확인할 수 있도록 설정
            processBuilder.environment().put("PYTHONIOENCODING", "utf-8"); // 환경 변수로 UTF-8 인코딩 설정

            return new YoutubeDownloadResponse(executeProcess(processBuilder, "youtubeDownload"));
        } catch (Exception e) {
            System.err.println(DEFAULT_LOG_EXCEPTION + "[youtubeDownload] " + e.getMessage());
            return new YoutubeDownloadResponse(false, DEFAULT_LOG_EXCEPTION + "[youtubeDownload] " + e.getMessage());
        }
    }

    /**
     * 노래 Info를 벅스 데이터 기반으로 업데이트
     * @return - 다운로드 mpe 파일 업데이트 결과
     */
    public UpdateSongInfoResponse updateSongInfo() {
        try {
            // 앨범 info 업데이트 part
            String updateScript = String.valueOf(Paths.get(externalResourcePath, "scripts/UpdateEveryMp3FileTag.py"));
            String mp3Path = String.valueOf(Paths.get(externalResourcePath + "/mp3file"));

            ProcessBuilder processBuilder = new ProcessBuilder("python", updateScript, "--mp3path", mp3Path);

            return new UpdateSongInfoResponse(executeProcess(processBuilder, "updateSongInfo"));
        } catch (Exception e) {
            System.err.println(DEFAULT_LOG_EXCEPTION + "[updateSongInfo] " + e.getMessage());
            return new UpdateSongInfoResponse(false, DEFAULT_LOG_EXCEPTION + "[updateSongInfo] " + e.getMessage());
        }
    }
}
