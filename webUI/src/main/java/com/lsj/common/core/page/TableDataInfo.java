package com.lsj.common.core.page;

import java.io.Serializable;
import java.util.List;

/**
 * 테이블 페이징 데이터 객체
 */
public class TableDataInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 총 기록 수
     */
    private long total;

    /**
     * 목록 데이터
     */
    private List<?> rows;

    /**
     * 메시지 상태 코드
     */
    private int code;

    /**
     * 메시지 내용
     */
    private String msg;

    /**
     * 테이블 데이터 객체
     */
    public TableDataInfo() {
    }

    /**
     * 페이징
     *
     * @param list  목록 데이터
     * @param total 총 기록 수
     */
    public TableDataInfo(List<?> list, int total) {
        this.rows = list;
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
