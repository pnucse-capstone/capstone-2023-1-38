package com.lsj.common.core.domain.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.lsj.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 열할 객체 sys_role
 */
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 역할 ID
     */
    private Long roleId;

    /**
     * 역할 이름
     */
    private String roleName;

    /**
     * 역할 권한
     */
    private String roleKey;

    /**
     * 역할 정열
     */
    private String roleSort;

    /**
     * 데이터 범위 (1:모든 데이터 권한; 2:사용자 지정 데이터 권한; 3:본 부서 데이터 권한)
     */
    private String dataScope;

    /**
     * 메뉴 트리 선택항 관련 여부 표시 (0:부모 자식 간 관련 안 함; 1:부모 자식 간 관련함)
     */
    private boolean menuCheckStrictly;

    /**
     * 부서 트리 선택항 관련 여부 표시 (0:부모 자식 비관련 표시; 1:부모 자식 간 상호관련 표시)
     */
    private boolean deptCheckStrictly;

    /**
     * 사용자 상태 (0:정상; 1:정지)
     */
    private String status;

    /**
     * 표시 삭제 (0:존재; 2:삭제)
     */
    private String delFlag;

    /**
     * 사용자가 역할 표시 가지고 있는지 확인 (기본값:존재하지 않음)
     */
    private boolean flag = false;

    /**
     * 메뉴 그룹
     */
    private Long[] menuIds;

    /**
     * 부서 그룹(데이터 권한)
     */
    private Long[] deptIds;

    public SysRole() {

    }

    public SysRole(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public boolean isAdmin() {
        return isAdmin(this.roleId);
    }

    public static boolean isAdmin(Long roleId) {
        return roleId != null && 1L == roleId;
    }

    @NotBlank(message = "사용자 이름은 비어 둘 수 없습니다")
    @Size(min = 0, max = 30, message = "사용자 이름 길이는 30자를 초과 할 수 없습니다")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @NotBlank(message = "권한 문자는 비어둘 수 없습니다")
    @Size(min = 0, max = 100, message = "권한 문자 길이는 100자를 초과할 수 없습니다")
    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    @NotBlank(message = "표시 순서는 비워둘 수 없습니다")
    public String getRoleSort() {
        return roleSort;
    }

    public void setRoleSort(String roleSort) {
        this.roleSort = roleSort;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public boolean isMenuCheckStrictly() {
        return menuCheckStrictly;
    }

    public void setMenuCheckStrictly(boolean menuCheckStrictly) {
        this.menuCheckStrictly = menuCheckStrictly;
    }

    public boolean isDeptCheckStrictly() {
        return deptCheckStrictly;
    }

    public void setDeptCheckStrictly(boolean deptCheckStrictly) {
        this.deptCheckStrictly = deptCheckStrictly;
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

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long[] getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(Long[] menuIds) {
        this.menuIds = menuIds;
    }

    public Long[] getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(Long[] deptIds) {
        this.deptIds = deptIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId", getRoleId())
                .append("roleName", getRoleName())
                .append("roleKey", getRoleKey())
                .append("roleSort", getRoleSort())
                .append("dataScope", getDataScope())
                .append("menuCheckStrictly", isMenuCheckStrictly())
                .append("deptCheckStrictly", isDeptCheckStrictly())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
