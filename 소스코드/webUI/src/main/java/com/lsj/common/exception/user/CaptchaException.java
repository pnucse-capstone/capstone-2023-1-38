package com.lsj.common.exception.user;

/**
 * 인증코드 오류 예외 클래스
 */
public class CaptchaException extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaException() {
        super("user.jcaptcha.error", null);
    }
}
