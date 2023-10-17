package com.lsj.common.utils;

/**
 * 로그 파일 처리 및 기록
 * 로그에 정보를 기록하기 위해 개체를 형식이 지정된 문자열로 변환하는 데 사용
 */
public class LogUtils {
    public static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
