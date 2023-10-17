package com.lsj.common.core.domain.entity;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.lsj.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 사용자 객체 sys_user
 */
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 사용자 번호
     */
    private Long userId;

    /**
     * 부서 ID
     */
    private Long deptId;

    /**
     * 사용자 계정번호
     */
    private String userName;


    /**
     * 사용자 닉네임
     */
    private String nickName;

    /**
     * 사용자 이메일
     */
    private String email;

    /**
     * 전화번호
     */
    private String phonenumber;

    /**
     * 사용자 성별
     */
    private String sex;

    /**
     * 사용자 프로필사진
     */
    private String avatar;

    /**
     * 비밀번호
     */
    private String password;

    /**
     * 암호화 솔트
     */
    private String salt;

    /**
     * 계정 상태 (0정상 1정지)
     */
    private String status;

    /**
     * 삭제 표시 (0존재 2삭제)
     */
    private String delFlag;

    /**
     * 부서
     */
    private SysDept dept;

    /**
     * 역할 객체
     */
    private List<SysRole> roles;

    /**
     * 역할 그룹
     */
    private Long[] roleIds;

    /**
     * 직위 그룹
     */
    private Long[] postIds;

    public SysUser() {

    }

    public SysUser(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) { //사용자가 관리자인지 확인
        return userId != null && 1L == userId;//1L:Long 타임
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Size(min = 0, max = 30, message = "사용자 닉네임 길이는 30를 초과할 수 없습니다.")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @NotBlank(message = "사용자 계정번호는 비워둘 수 없습니다.")
    @Size(min = 0, max = 30, message = "사용자 계정번호 길이는 30자를 초과할 수 없습니다.")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Email(message = "이메일 형식이 잘못되었습니다.")
    @Size(min = 0, max = 50, message = "이메일 길이는 50자를 초과할 수 없습니다.")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Size(min = 0, max = 11, message = "휴대폰 번호 길이는 11자를 초과할 수 없습니다.")
    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonIgnore//직렬화 및 역직렬화 중에 다음 정보를 무시(비밀번호 노출 방지)
    @JsonProperty
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public SysDept getDept() {
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds() {
        return postIds;
    }

    public void setPostIds(Long[] postIds) {
        this.postIds = postIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("deptId", getDeptId())
                .append("userName", getUserName())
                .append("nickName", getNickName())
                .append("email", getEmail())
                .append("phonenumber", getPhonenumber())
                .append("sex", getSex())
                .append("avatar", getAvatar())
                .append("password", getPassword())
                .append("salt", getSalt())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("dept", getDept())
                .toString();
    }
}
