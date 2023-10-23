package com.lsj.common.core.page;

import com.lsj.common.utils.ServletUtils;

/**
 * 테이블 데어터 처리
 */
public class TableSupport {
    /**
     * 현재 기록의 시작 인덱스
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 한 페이지에 나타내는 기록수
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 열 정렬
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 정렬 방향 "desc" 내린차순 "asc" 오름차순
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 페이징 객체를 갭슐화
     */
    public static PageDomain getPageDomain() {
        PageDomain pageDomain = new PageDomain();//pageDomain 객체를 생성
        pageDomain.setPageNum(ServletUtils.getParameterToInt(PAGE_NUM));//요청에서 제이지 번호를 얻기
        pageDomain.setPageSize(ServletUtils.getParameterToInt(PAGE_SIZE));//요청에서 한 페이지 나타내는 기록수를 얻기
        pageDomain.setOrderByColumn(ServletUtils.getParameter(ORDER_BY_COLUMN));//요청에서 열 정렬을 얻기
        pageDomain.setIsAsc(ServletUtils.getParameter(IS_ASC));//요청에서 나타내는 차순을 얻기
        return pageDomain;
    }

    public static PageDomain buildPageRequest() {
        return getPageDomain();
    }
}
