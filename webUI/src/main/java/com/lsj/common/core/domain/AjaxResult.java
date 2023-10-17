package com.lsj.common.core.domain;

import java.util.HashMap;

import com.lsj.common.constant.HttpStatus;
import com.lsj.common.utils.StringUtils;

/**
 * 액선 메시지 제시
 */
public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /**
     * 상태 코드
     */
    public static final String CODE_TAG = "code";

    /**
     * 반납 내용
     */
    public static final String MSG_TAG = "msg";

    /**
     * 데이터 객체
     */
    public static final String DATA_TAG = "data";

    /**
     * 새로 생성된 AjaxResult 객체를 초기화하여 빈 메시지를 표시
     */
    public AjaxResult() {}

    /**
     * 새로 생성된 AjaxResult 객체 초기화
     *
     * @param code 상태 code
     * @param msg  반납 내용
     */
    public AjaxResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 새로 생성된 AjaxResult 객체 초기화
     *
     * @param code 상태 code
     * @param msg  반납 내용
     * @param data 데이터 객체
     */
    public AjaxResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);//CODE_TAG:속성 code:속성값
        super.put(MSG_TAG, msg);//MSG_TAG:속성 code:속성값
        if (StringUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);//DATA_TAG속성 data:속성값
        }
    }

    /**
     * 성공 메시지 반환
     *
     * @return 성공 메시지
     */
    public static AjaxResult success() {
        return AjaxResult.success("액션 성공");
    }

    /**
     * 성공 메시지 반환
     *
     * @return 성공 메시지
     */
    public static AjaxResult success(Object data) {
        return AjaxResult.success("액션 성공", data);
    }

    /**
     * 성공 메시지 반환
     *
     * @param msg 반납 내용
     * @return 성공 메시지
     */
    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }

    /**
     * 성공 메시지 반환
     *
     * @param msg  반납 내용
     * @param data 데이터 객체
     * @return 성공 메시지
     * AjaxResult 객체는 백엔드가 프런트 엔드에 데이터를 반환하는 모델
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 오류 메시지 반환
     *
     * @return 오류 메시지
     */
    public static AjaxResult error() {
        return AjaxResult.error("액션 실패");
    }

    /**
     * 오류 메시지 반환
     *
     * @param msg 난납 내용
     * @return warning 메시지
     */
    public static AjaxResult error(String msg) {
        return AjaxResult.error(msg, null);
    }

    /**
     * 오류 메시지 반환
     *
     * @param msg  난납 내용
     * @param data 데이터 객체
     * @return warning 메시지
     */
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }

    /**
     * 오류 메시지 반환
     *
     * @param code 상태 code
     * @param msg  반납 내용
     * @return warning 메시지
     */
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, null);
    }
}
