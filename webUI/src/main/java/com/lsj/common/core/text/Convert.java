package com.lsj.common.core.text;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.lsj.common.utils.StringUtils;

/**
 * 유형 변환기
 */
public class Convert {
    /**
     * 문자열로 변환
     * 주어진 값이 null이거나 변환에 실패하면 기본값을 반환
     * 변환에 실패하면 오류가 보고되지 않음
     *
     * @param value        변환된 값
     * @param defaultValue 변환 오류 시 반납하는기본값
     * @return 결과
     */
    public static String toStr(Object value, String defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        if (value instanceof String) { //주어진 값 value가 String 유형이면 직접 String으로 캐스트되어 반환
            return (String) value;
        }
        return value.toString();
    }

    /**
     * int로 전환하기
     * 주어진 값은 null거나 변환에 실패하면 기본값으로 반납
     * 전환 실패하면 오류를 보고하지 않음
     *
     * @param value        전환 된 값
     * @param defaultValue 전환 오류시 반납하는 기본값
     * @return 결과
     */
    public static Integer toInt(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(valueStr.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * int로 전환하기
     * 주어진 값이 <code>null</code>이거나 변환 실패하면 기본값을 <code>null</code>를 반납
     * 전환 실패하면 오류를 보고하지 않음
     *
     * @param value 전환된 값
     * @return 결과
     */
    public static Integer toInt(Object value) {
        return toInt(value, null);
    }


    public static String utf8Str(Object obj) {
        return str(obj, Charset.forName("UTF-8"));
    }

    public static String str(Object obj, String charsetName) {
        return str(obj, Charset.forName(charsetName));
    }

    public static String str(Object obj, Charset charset) {
        if (null == obj) {
            return null;
        }

        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof byte[] || obj instanceof Byte[]) {
            return str((Byte[]) obj, charset);
        } else if (obj instanceof ByteBuffer) {
            return str((ByteBuffer) obj, charset);
        }
        return obj.toString();
    }
    public static String str(byte[] bytes, String charset) {
        return str(bytes, StringUtils.isEmpty(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    public static String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }

        if (null == charset) {
            return new String(data);
        }
        return new String(data, charset);
    }

    public static String str(ByteBuffer data, String charset) {
        if (data == null) {
            return null;
        }

        return str(data, Charset.forName(charset));
    }

    public static String str(ByteBuffer data, Charset charset) {
        if (null == charset) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }
}
