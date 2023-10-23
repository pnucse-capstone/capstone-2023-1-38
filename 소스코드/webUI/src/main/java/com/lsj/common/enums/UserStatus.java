package com.lsj.common.enums;

/**
 * 사용자 상태
 */
public enum UserStatus {
    OK("0", "정상"), DISABLE("1", "정지"), DELETED("2", "삭제");

    private final String code;
    private final String info;

    UserStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
