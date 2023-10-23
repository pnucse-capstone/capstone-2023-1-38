package com.lsj.common.constant;

/**
 * 통용 상수 정보
 */
public class Constants {
    /**
     * UTF-8 character set
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK character set
     */
    public static final String GBK = "GBK";

    /**
     * http request
     */
    public static final String HTTP = "http://";

    /**
     * https request
     */
    public static final String HTTPS = "https://";

    /**
     * 통용 성공 표시
     */
    public static final String SUCCESS = "0";

    /**
     * 통용 실패 표시
     */
    public static final String FAIL = "1";

    /**
     * 로그인 성공
     */
    public static final String LOGIN_SUCCESS = "Success";


    public static final String LOGOUT = "Logout";

    /**
     * 로그인 실패
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 인증 코드 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 로그인 사용자 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 중복 제출 방식 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 인증코드 유효기간（분）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * token
     */
    public static final String TOKEN = "token";

    /**
     * token prefix
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * token prefix
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 사용자 ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 사용자 이름
     */
    public static final String JWT_USERNAME = "sub";

    /**
     * 사용자 프로필 사진
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 생성 시간
     */
    public static final String JWT_CREATED = "created";

    /**
     * 사용자 권한
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 파라미터 관리 캐시 키
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * dict 관리 캐시 키
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 리소스 매핑 경로 prefix
     */
    public static final String RESOURCE_PREFIX = "/profile";
}





