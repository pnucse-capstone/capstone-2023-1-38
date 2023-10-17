package com.lsj.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 프로젝트 관련 구성 읽기
 */
@Component
@ConfigurationProperties(prefix = "lsj")
public class lsjConfig {

    /**
     * 저작권 연도
     */
//    private String copyrightYear;


    /**
     * 업로드 경로
     */
    private static String profile;


    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        lsjConfig.profile = profile;
    }

    /**
     * 프로필 사진 업로드 경로 얻기
     */
    public static String getAvatarPath() {
        return getProfile() + "/avatar";
    }


    /**
     * 업로드 경로 얻기
     */
    public static String getUploadPath() {
        return getProfile() + "/upload";
    }
}





