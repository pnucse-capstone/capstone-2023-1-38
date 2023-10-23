package com.lsj.common.exception.user;

import com.lsj.common.exception.BaseException;

/**
 * 사용자 정보 예외 클래스
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
