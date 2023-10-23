package com.lsj.common.core.domain.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.lsj.common.constant.UserConstants;
import com.lsj.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 사전 데이터 테이블  sys_dict_data
 */
public class SysDictData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 사전 코드
     */
    private Long dictCode;

    /**
     * 사전 정렬
     */
    private Long dictSort;

    /**
     * 사전 태그
     */
    private String dictLabel;

    /**
     * 사전 키값
     * 字典键值
     */
    private String dictValue;

    /**
     * 사전 유형
     */
    private String dictType;

    /**
     * 스타일 속성(추가 스타일 확장)
     */
    private String cssClass;

    /**
     * 테이블 사전 스타일
     */
    private String listClass;

    /**
     * 기본값인지 여부( Y여 N부)
     */
    private String isDefault;

    /**
     * 상태 (0정상 1정지)
     */
    private String status;

    public Long getDictCode() {
        return dictCode;
    }

    public void setDictCode(Long dictCode) {
        this.dictCode = dictCode;
    }

    public Long getDictSort() {
        return dictSort;
    }

    public void setDictSort(Long dictSort) {
        this.dictSort = dictSort;
    }

    @NotBlank(message = "사전 태그는 비워둘 수 없습니다")
    @Size(min = 0, max = 100, message = "사전 태그는 길이가 100자를 초과할 수 없습니다")
    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    @NotBlank(message = "사전 키값은 비워둘 수 없습니다")
    @Size(min = 0, max = 100, message = "사전 키값 길이는 100자를 초과할 수 없습니다")
    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    @NotBlank(message = "사전 유형은 비워둘 수 없습니다")
    @Size(min = 0, max = 100, message = "사전 유형의 길이는 100자를 초과할 수 없습니다")
    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    @Size(min = 0, max = 100, message = "스타일 속성은 100자를 초과할 수 없습니다")
    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getListClass() {
        return listClass;
    }

    public void setListClass(String listClass) {
        this.listClass = listClass;
    }

    public boolean getDefault() {
        return UserConstants.YES.equals(this.isDefault) ? true : false;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("dictCode", getDictCode())
                .append("dictSort", getDictSort())
                .append("dictLabel", getDictLabel())
                .append("dictValue", getDictValue())
                .append("dictType", getDictType())
                .append("cssClass", getCssClass())
                .append("listClass", getListClass())
                .append("isDefault", getIsDefault())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
