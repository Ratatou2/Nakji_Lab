package com.nakji.lab.utill;

import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
    /**
     * 현재 운영체제가 리눅스인지 체크
     * @return - 현 운영체제가 리눅스인지 여부
     */
    public static boolean isLinux() {
        String osName = System.getProperty("os.name").toLowerCase();  // 운영체제 정보 가져오기
        return (osName.contains("nix") || osName.contains("nux") || osName.contains("mac"));
    }
}
