package com.lsj.common.core.page;

import com.lsj.common.utils.StringUtils;

/**
 * 페이징 데이터
 */
public class PageDomain {
    /**
     * 현재 기록의 시작 인덱스
     */
    private Integer pageNum;

    /**
     * 한 페이지에 나타내는 기록수
     */
    private Integer pageSize;

    /**
     * 열 정렬
     */
    private String orderByColumn;

    /**
     * 정렬 방향 "desc" 내린차순 "asc" 오름차순
     */
    private String isAsc = "asc";

    public String getOrderBy() {
        if (StringUtils.isEmpty(orderByColumn)) {
            return "";
        }
        return StringUtils.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public String getIsAsc() {
        return isAsc;
    }

    public void setIsAsc(String isAsc) {
        this.isAsc = isAsc;
    }
}
