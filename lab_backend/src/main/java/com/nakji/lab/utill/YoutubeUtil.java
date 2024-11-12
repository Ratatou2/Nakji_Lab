package com.nakji.lab.utill;

import org.springframework.stereotype.Component;

@Component
public class YoutubeUtil {
    public static boolean isValidUrl(String url) {
        String regex = "^(https?://)?(www\\.)?(youtube|youtu|youtube-nocookie)\\.com.*$";
        return url != null && url.matches(regex);
    }
}
