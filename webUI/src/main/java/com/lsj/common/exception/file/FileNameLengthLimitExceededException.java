package com.lsj.common.exception.file;

/**
 * 파일명 길이 제한 예외 클래스
 */
public class FileNameLengthLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[]{defaultFileNameLength});
    }
}
