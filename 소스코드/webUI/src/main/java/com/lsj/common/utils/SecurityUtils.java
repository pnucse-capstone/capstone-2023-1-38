package com.lsj.common.utils;

import com.lsj.common.constant.HttpStatus;
import com.lsj.common.core.domain.model.LoginUser;
import com.lsj.common.exception.CustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security Service 도구 클래스
 */
public class SecurityUtils {

    /**
     * 사용자 계정 얻기
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            throw new CustomException("사용자 계정 얻기 오류", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 로그인 사용자 얻기
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();//从当前的安全上下文中获取已经经过认证的登陆用户信息
        } catch (Exception e) {
            throw new CustomException("사용자 정보 얻기 오류", HttpStatus.UNAUTHORIZED);
        }
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();//SecurityContext 객체에서 Authentication얻기 从上下文对象中获取Authentication
    }

    public static String encryptPassword(String password) {
        //BCryptPasswordEncoder는 Spring Security에서 암호화를 위한 구현 클래스이다. BCrypt 해시 알고리즘을 사용하여 암호를 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);//비밀번호를 암호화 하고 반납
    }

    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        //Spring Security에서 제공하는 BCryptPasswordEncoder 클래스는 암호를 암호화하고 일치시키는 데 사용
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //원래 비밀번호가 암호화된 비밀번호와 일치하면 matches 메서드는 true를 반납, 그렇지 않으면 false를 반납
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 관리자인지 여부
     *
     * @param userId 사용자 ID
     * @return 결과
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}
