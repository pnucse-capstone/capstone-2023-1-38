package com.lsj.common.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity 엔터티 기반 클래스
 */
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 검색 값:
     * 조회 조건의 값을 저장용, 조회 조작에 사용
     */
    private String searchValue;

    /**
     * 생성자
     * 엔터티 객체의 생성자를 기록용
     */
    private String createBy;

    /**
     * 시간 생성
     * 엔터티 객체의 생성 시간을 기록용
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") //날짜/시간 유형 필드의 직렬화 및 역직렬화 형식을 제어하는 데 사용
    private Date createTime;

    /**
     * 업데이트하는 사람
     */
    private String updateBy;

    /**
     * 시간 업데이트
     * 엔터티 개체를 업데이트하는 시간을 기록용
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 비고
     * 엔터티 객체의 비고 정보를 저장
     */
    private String remark;

    /**
     * 시작 시간
     * 시간 범위의 시작 시간을 저장
     */
    @JsonIgnore //직렬화 및 역직렬화 중에 속성이나 메서드를 무시하는 데 사용
    private String beginTime;

    /**
     * 종료 시간
     * 시간 범위의 종료 시간
     */
    @JsonIgnore
    private String endTime;

    /**
     * 요청 매개변수
     * 다른 스스로 정의한 요청 매개변수를 저장(키값 쌍 형식)
     */
    private Map<String, Object> params;

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
