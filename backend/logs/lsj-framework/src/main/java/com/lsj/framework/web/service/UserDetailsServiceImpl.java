package com.lsj.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.lsj.common.core.domain.entity.SysUser;
import com.lsj.common.core.domain.model.LoginUser;
import com.lsj.common.enums.UserStatus;
import com.lsj.common.exception.BaseException;
import com.lsj.common.utils.StringUtils;
import com.lsj.system.service.ISysUserService;

/**
 * 사용자 인증 처리
 */
@Service //Spring 컨테이너에 서비스 빈으로 등록
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //사용자 이름으로 사용자 테이블을 조회하여 현재 로그인한 사용자를 얻기
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user)) { //사용자 정보 null이면 예외 발생
            log.info("사용자：{} 존재하지 않습니다.", username);
            throw new UsernameNotFoundException("로그인 사용자：" + username + " 존재하지 않습니다");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) { //사용자 삭제 표시되면 예외 발생
            log.info("로그인 사용자：{} 삭제되었습니다.", username);
            throw new BaseException(username + "계정은 삭제되었습니다");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) { //사용자 정지 상태이면 예외 발생
            log.info("로그인 사용자：{} 정지되었습니다.", username);
            throw new BaseException(username + "계정은 정지되었습니다");
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user, permissionService.getMenuPermission(user));//사용자 정보+권한 정보
    }
}
