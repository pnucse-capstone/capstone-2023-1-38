package com.lsj.common.constant;

/**
 * 반환 상태 코드
 */
public class HttpStatus {
    /**
     * 조작 성공
     */
    public static final int SUCCESS = 200;

    /**
     * 객체 생성 성공
     */
    public static final int CREATED = 201;

    /**
     * 요청이 접수된다
     */
    public static final int ACCEPTED = 202;

    /**
     * 조작이 성공적으로 실행되었지만 반환된 데이터가 없다
     */
    public static final int NO_CONTENT = 204;

    /**
     * 데이터를 제거되었다
     */
    public static final int MOVED_PERM = 301;

    /**
     * 재지정
     */
    public static final int SEE_OTHER = 303;

    /**
     * 리소스 수정되지 않았다
     */
    public static final int NOT_MODIFIED = 304;

    /**
     * 파라미터 목록 오류 (모자르거나 형식 불일치)
     */
    public static final int BAD_REQUEST = 400;

    /**
     * 허가되지 않음
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 접근 제한, 권한 만료
     */
    public static final int FORBIDDEN = 403;

    /**
     * 리소스, 서비스 찾을 수 없다
     */
    public static final int NOT_FOUND = 404;

    /**
     * 허용되지 않는 HTTP 방법
     */
    public static final int BAD_METHOD = 405;

    /**
     * 리소스가 충돌하거나, 리소스가 잠겨 있다
     */
    public static final int CONFLICT = 409;

    /**
     * 지원되지 않는 데이터, 미디어 유형
     */
    public static final int UNSUPPORTED_TYPE = 415;

    /**
     * 시스템 내부 오류
     */
    public static final int ERROR = 500;

    /**
     * 인터페이스가 구현되지 않음
     */
    public static final int NOT_IMPLEMENTED = 501;
}





