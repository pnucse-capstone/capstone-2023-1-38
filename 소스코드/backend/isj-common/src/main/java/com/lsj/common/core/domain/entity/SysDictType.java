package com.lsj.common.core.domain.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.lsj.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 사전 유형 테이블 sys_dict_type
 */
public class SysDictType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 사전 키
     */
    private Long dictId;

    /**
     * 사전 이름
     */
    private String dictName;

    /**
     * 사전 유형
     */
    private String dictType;

    /**
     * 상태 (0정상 1정지)
     */
    private String status;

    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    @NotBlank(message = "사전 이름은 비워 둘 수 없습니다")
    @Size(min = 0, max = 100, message = "사전 유형 이름 길이는 100자를 초과할 수 없습니다")
    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    @NotBlank(message = "사전 유형은 비워둘 수 없습니다")
    @Size(min = 0, max = 100, message = "사전 유형 길이는 100자를 초과할 수 없습니다.")
    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
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
                .append("dictId", getDictId())
                .append("dictName", getDictName())
                .append("dictType", getDictType())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}

