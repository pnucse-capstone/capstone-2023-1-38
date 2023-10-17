package com.lsj.common.exception.file;

/**
 * 파일 이름 크기 제한 예외 클래스
 */
public class FileSizeLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("upload.exceed.maxSize", new Object[]{defaultMaxSize});
    }
}
