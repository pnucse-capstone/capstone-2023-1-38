package com.lsj.common.core.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.lsj.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 부서 table sys_dept
 */
public class SysDept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 부서 ID
     */
    private Long deptId;

    /**
     * 상위 부서 ID
     */
    private Long parentId;

    /**
     * 조상 목록
     */
    private String ancestors;

    /**
     * 부서 이름
     */
    private String deptName;

    /**
     * 표시 순서
     */
    private String orderNum;

    /**
     * 담당자
     */
    private String leader;

    /**
     * 연락 번호
     */
    private String phone;

    /**
     * 이메일
     */
    private String email;

    /**
     * 부서 상태: 0정상 1정지
     */
    private String status;

    /**
     * 삭제표시 (0존재 2삭제)
     */
    private String delFlag;

    /**
     * 상위 부서 이름
     */
    private String parentName;

    /**
     * 자식 부서
     */
    private List<SysDept> children = new ArrayList<SysDept>();

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    @NotBlank(message = "부서 이름은 비워 둘 수 없습니다")
    @Size(min = 0, max = 30, message = "부서 이름 길이는 30자를 초과할 수 없습니다")
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @NotBlank(message = "표시 순서는 비워둘 수 없습니다")
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    @Size(min = 0, max = 11, message = "전화번호 길이는 11자를 초과할 수 없습니다")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Email(message = "이메일 형식이 올바르지 않습니다")
    @Size(min = 0, max = 50, message = "이메일 길이는 50자를 초과할 수 없습니다")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<SysDept> getChildren() {
        return children;
    }

    public void setChildren(List<SysDept> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("deptId", getDeptId())
                .append("parentId", getParentId())
                .append("ancestors", getAncestors())
                .append("deptName", getDeptName())
                .append("orderNum", getOrderNum())
                .append("leader", getLeader())
                .append("phone", getPhone())
                .append("email", getEmail())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
