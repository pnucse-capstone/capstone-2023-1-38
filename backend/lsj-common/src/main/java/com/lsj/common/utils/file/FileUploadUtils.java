package com.lsj.common.utils.file;

import java.io.File;
import java.io.IOException;

import com.lsj.common.constant.Constants;
import com.lsj.common.exception.file.FileNameLengthLimitExceededException;
import com.lsj.common.exception.file.FileSizeLimitExceededException;
import com.lsj.common.exception.file.InvalidExtensionException;
import com.lsj.common.utils.DateUtils;
import com.lsj.common.utils.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import com.lsj.common.config.lsjConfig;
import com.lsj.common.utils.uuid.IdUtils;

/**
 * 파일 업로드 도구 클래스
 */
public class FileUploadUtils {
    /**
     * 기본 크기 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 기본 파일 이름 최대 길이 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;


    /**
     * 파일 경로 따라 업로드
     *
     * @param baseDir 파일 업로드 경로
     * @param file    업로드할 파일
     * @return 파일 이름
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException {
        try {
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException {
        int fileNamelength = file.getOriginalFilename().length();
        if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file, allowedExtension);

        String fileName = extractFilename(file);

        File desc = getAbsoluteFile(baseDir, fileName);
        file.transferTo(desc);
        String pathFileName = getPathFileName(baseDir, fileName);
        return pathFileName;
    }

    /**
     * 파일 이름 인코딩
     */
    public static final String extractFilename(MultipartFile file) {
        String fileName = file.getOriginalFilename();//getOriginalFilename():업로드된 파일의 원래 파일 이름을 얻기
        String extension = getExtension(file);//파일 이름의 접미사 얻기
        //파일명: 날짜+uuid+접미사
        fileName = DateUtils.datePath() + "/" + IdUtils.fastUUID() + "." + extension;
        return fileName;
    }

    private static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }

    private static final String getPathFileName(String uploadDir, String fileName) throws IOException {
        int dirLastIndex = lsjConfig.getProfile().length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
        return pathFileName;
    }

    public static final void assertAllowed(MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, InvalidExtensionException {
        long size = file.getSize();//파일 크기 얻기
        if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE) { //파일 크기 초과하면
            throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);//파일 크기 제한에 초과 예외
        }

        String fileName = file.getOriginalFilename();//getOriginalFilename():업로드된 파일의 원래 파일 이름을 얻기
        String extension = getExtension(file);//파일 이름의 접미사 얻기
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension,
                        fileName);
            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,
                        fileName);
            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,
                        fileName);
            } else {
                throw new InvalidExtensionException(allowedExtension, extension, fileName);
            }
        }

    }

    /**
     * MIME 유형이 허용된 MIME 유형인지 확인 判断MIME类型是否是允许的MIME类型
     *
     * @param extension 확인할 파일 확장자
     * @param allowedExtension 업로드 파일 유형
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) { //문자열 str과 extension이 같은지 비교(대소문자 무시)
                return true;
            }
        }
        return false;
    }

    /**
     * 파일 이름의 접미사 얻기 获取文件名的后缀
     *
     * @param file 테이블 파일 表单文件
     * @return 접미사 后缀名
     */
    public static final String getExtension(MultipartFile file) {
        //getOriginalFilename():업로드된 파일의 원래 파일 이름을 얻기
        //FilenameUtils.getExtension:파일 객체의 원래 파일 이름에서 확장자를 추출,없으면 빈 문자열 반납
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) { //파일 확장자 없으면
            //getContentType():업로드한 파일 유형을 얻기
            //getExtension:파일 유형따라 파일 접미사를 얻기
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }
}
