package com.nakji.lab.dto.request;

import lombok.Getter;

@Getter
public class YoutubeDownloadRequest {
    String url;
    String artist;
    String songTitle;

    @Override
    public String toString() {
        return "==== [Info] ====" + "\n"
                + "ARTIST : " + artist + "\n"
                + "SONG_TITLE : " + songTitle + "\n"
                + "URL : " + url + "\n";
    }
}
