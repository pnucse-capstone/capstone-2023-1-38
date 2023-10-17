package com.lsj.common.exception.user;

/**
 * 사용자 비밀번로 오바르지 않거나 규범 위반 예외 클래스
 */
public class UserPasswordNotMatchException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException() {
        super("user.password.not.match", null);
    }
}
