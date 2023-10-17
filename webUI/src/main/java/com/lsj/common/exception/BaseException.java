package com.lsj.common.exception;

import com.lsj.common.utils.StringUtils;
import com.lsj.common.utils.MessageUtils;

/**
 * 기본 예외 클래스
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 소속 모듈
     */
    private String module;

    /**
     * 에러 코드
     */
    private String code;

    /**
     * 오류 코드 해당 하는 파라미터
     */
    private Object[] args;

    /**
     * 오류 메시지
     */
    private String defaultMessage;

    public BaseException(String module, String code, Object[] args, String defaultMessage) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public BaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }

    @Override
    public String getMessage() {
        String message = null;
        if (!StringUtils.isEmpty(code)) {
            message = MessageUtils.message(code, args);
        }
        if (message == null) {
            message = defaultMessage;
        }
        return message;
    }


    public String getCode() {
        return code;
    }
}
