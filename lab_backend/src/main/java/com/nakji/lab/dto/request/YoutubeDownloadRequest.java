package com.nakji.lab.dto.request;

public record YoutubeDownloadRequest(
        String url,
        String singer,
        String songName
) {
    @Override
    public String toString() {
        return "==== [Info] ====" + "\n"
                + "SINGER : " + singer + "\n"
                + "SONG_NAME : " + songName + "\n"
                + "URL : " + url + "\n";
    }
}
