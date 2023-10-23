package com.lsj.common.utils.sql;

import com.lsj.common.exception.BaseException;
import com.lsj.common.utils.StringUtils;

/**
 * sql 조작 도수 클래스
 */
public class SqlUtil {
    /**
     *문자, 숫자, 밑줄, 공백, 쉼표, 소수점만 지원(여러 필드 정렬 지원)
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * 문자 체크, 주입 우회 방지
     */
    public static String escapeOrderBySql(String value) {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value)) {
            throw new BaseException("파라미터가 규격에 맞지 않아, 조회할 수 없습니다");
        }
        return value;
    }

    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }
}
