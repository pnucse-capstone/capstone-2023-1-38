package com.lsj.common.exception.file;

import com.lsj.common.exception.BaseException;

/**
 * 파일 정보 예외 클래스
 */
public class FileException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
