package com.lsj.common.core.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

import com.lsj.common.constant.HttpStatus;
import com.lsj.common.utils.DateUtils;
import com.lsj.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lsj.common.core.domain.AjaxResult;
import com.lsj.common.core.page.PageDomain;
import com.lsj.common.core.page.TableDataInfo;
import com.lsj.common.core.page.TableSupport;
import com.lsj.common.utils.sql.SqlUtil;

/**
 * 웹 층에서 통용 데이터를 처리
 */
public class BaseController {

    /**
     * 프런트에서 전달된 날짜 형식 문자열을 Date 형식으로 변환
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 요청 페이징 데이터를 설정
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();//페이징 정보를 얻기
        Integer pageNum = pageDomain.getPageNum();//페이지 번호를 얻기
        Integer pageSize = pageDomain.getPageSize();//페이지 전시 수량을 얻기
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) { //값이 다 있으면 페이징하기
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());//문자 체크, 주입 우회방지
            PageHelper.startPage(pageNum, pageSize, orderBy);//페이지의 번호, 수량, 순차 따라 페이징하기
        }
    }

    /**
     * 요청에 대한 페이징 데이터 응답
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    //@SuppressWarnings:지정된 유형에 대한 경고를 무시하도록 컴파일러에 지시하기
    //rawtypes:기본 유형 사용과 관련된 경고 억제
    //unchecked:확인되지 않은 작업과 관련된 경고 억제
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();//TableDataInfo 객체를 생성
        rspData.setTotal(new PageInfo(list).getTotal());//list 객체에서 기록수를 얻어서 총 기록 수를 설정
        rspData.setRows(list);//목록 데이터 설정
        rspData.setCode(HttpStatus.SUCCESS);//메시지 상태 코드 설정
        rspData.setMsg("조회 성공");//메시지 내용 설정
        return rspData;
    }

    /**
     * 응답 결과를 return
     * @param rows 영향 행수
     * @return 결과
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();//영향 행수 있으면 성공 없으면 실패
    }
}




