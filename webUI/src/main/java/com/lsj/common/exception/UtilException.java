package com.lsj.common.exception;

/**
 * util 예외
 */
public class UtilException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(Throwable e) {
        super(e.getMessage(), e);
    }
}
