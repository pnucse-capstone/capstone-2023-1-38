package com.lsj.common.exception.user;

/**
 * 인증코드 실패 예외 클래스
 */
public class CaptchaExpireException extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException() {
        super("user.jcaptcha.expire", null);
    }
}
