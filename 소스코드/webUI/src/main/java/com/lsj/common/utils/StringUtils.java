package com.lsj.common.utils;

import java.util.Collection;
import java.util.Map;

import com.lsj.common.core.text.StrFormatter;

/**
 * 문자열 도구 클래스 字符串工具类
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 빈 문자열
     */
    private static final String NULLSTR = "";

    /**
     * 밑줄
     */
    private static final char SEPARATOR = '_';

    /**
     * * Collection null인지 판단, List，Set，Queue 포함
     *
     * @param coll 판단할 Collection
     * @return true:null false:null아님
     */
    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * Collection null아닌지 판단, List，Set，Queue 포함
     *
     * @param coll 판단할 Collection
     * @return true: null아님 false:null
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * * 객체 배열 비어 있는지 판단
     *
     * @param objects 판단할 객체 배열
     * @return true:null false:null아님
     */
    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }


    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }

    /**
     * 문자열 null 인지 확인
     *
     * @param str String
     * @return true：null 아님 false：null
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * 문자열 null 아닌지 확인
     *
     * @param str String
     * @return true：null 아님 false：null
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 객체가 null 인지 확인
     *
     * @param object Object
     * @return true：null 아님 false：null
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * 객체가 null 아닌지 확인
     *
     * @param object Object
     * @return true：null 아님 false：null
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    /**
     * 공백 제거
     */
    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }

    public static String substring(final String str, int start) {
        if (str == null) {
            return NULLSTR;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return NULLSTR;
        }

        return str.substring(start);
    }
    public static String format(String template, Object... params) {
        if (isEmpty(params) || isEmpty(template)) {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /**
     * 밑줄을 카멜 케이스로 변환
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        //앞 문자가 대문자
        boolean preCharIsUpperCase = true;
        //현재 문자가 대문자
        boolean curreCharIsUpperCase = true;
        //다음 문자가 대분자
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1)) {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }
}