package com.lsj.common.utils.uuid;

/**
 * ID 생성기 도구 클래스
 */
public class IdUtils {

    /**
     * 단순화 된 UUID, 가로선을 제거
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString(true);
    }

    /**
     * random UUID 생성, 더 나은 성능의 ThreadLocalRandom을 사용하여 UUID를 생성
     *
     * @return random UUID
     */
    public static String fastUUID() {
        return UUID.fastUUID().toString();
    }
}
