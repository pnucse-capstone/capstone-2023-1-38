package com.lsj.common.core.domain.model;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lsj.common.core.domain.entity.SysUser;

/**
 * 로그인 사용자 신분 권한
 */
public class LoginUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 사용자 유일 식별자
     */
    private String token;

    /**
     * 로그인 시간
     */
    private Long loginTime;

    /**
     * 타임아웃 주기
     */
    private Long expireTime;

    /**
     * 권한 list
     */
    private Set<String> permissions;

    /**
     * 사용자 정보
     */
    private SysUser user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginUser() {
    }

    public LoginUser(SysUser user, Set<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    @JsonIgnore //직렬화 및 역직렬화 중에는 무시
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    /**
     * 계정 기한 지나는지 확인, 만료되면 이증할 수 없다
     */
    @JsonIgnore //직렬화 및 역직렬화 중에는 무시
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }//security必须要实现的方法

    /**
     * 사용자의 잠금이 해제되어 있는지 확인, 잠긴 사용자는 인증할 수 없다
     */
    @JsonIgnore //직렬화 및 역직렬화 중에는 무시
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }//security必须要实现的方法

    /**
     * 사용자의 자격 증명(암호)이 만료되었는지 여부를 나타낸다.(만료된 자격 증명은 인증을 방지)
     */
    @JsonIgnore //직렬화 및 역직렬화 중에는 무시
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }//security必须要实现的方法

    /**
     * 정지된 사용자는 인증할 수 없다
     */
    @JsonIgnore //직렬화 및 역직렬화 중에는 무시
    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
