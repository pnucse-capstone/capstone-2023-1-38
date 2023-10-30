package com.lsj.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lsj.common.core.domain.BaseEntity;

/**
 * 직위 테이블 sys_post
 */
public class SysPost extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 직위 번호
     */
    private Long postId;

    /**
     * 직위 코드번호
     */
    private String postCode;

    /**
     * 직위 이름
     */
    private String postName;

    /**
     * 직위 정렬
     */
    private String postSort;

    /**
     * 상태（0정상 1정지）
     */
    private String status;

    /**
     * 사용자중에 직위 존재하는지 표시, 존재하지 않으로 기본값
     */
    private boolean flag = false;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @NotBlank(message = "직위 코드번호 비워둘 수 없습니다")
    @Size(min = 0, max = 64, message = "작위 코드번호 길이는 64자를 초과할 수 없습니다")
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @NotBlank(message = "직위 이름 비워둘 수 없습니다")
    @Size(min = 0, max = 50, message = "직위 이름 길이는 50자를 초과할 수 없습니다")
    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    @NotBlank(message = "표시 순서는 비워둘 수 없습니다")
    public String getPostSort() {
        return postSort;
    }

    public void setPostSort(String postSort) {
        this.postSort = postSort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("postId", getPostId())
                .append("postCode", getPostCode())
                .append("postName", getPostName())
                .append("postSort", getPostSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
