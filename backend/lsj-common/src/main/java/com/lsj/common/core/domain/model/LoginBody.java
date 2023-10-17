package com.lsj.common.core.domain.model;

/**
 * 사용자 로그인 객체
 */
public class LoginBody {
    /**
     * 사용자 이름
     */
    private String username;

    /**
     * 사용자 비밀번호
     */
    private String password;

    /**
     * 인증 코드
     */
    private String code;

    /**
     * 유일 식별자
     */
    private String uuid = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
