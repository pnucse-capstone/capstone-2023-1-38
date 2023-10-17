package com.lsj.framework.web.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.lsj.common.core.domain.model.LoginUser;
import com.lsj.common.utils.ServletUtils;
import com.lsj.common.utils.StringUtils;

/**
 * 권한 구현
 */
@Service("ss") //클래스를 Bean으로 인식하여 다른 컴포넌트에서 사용할 수 있도록 Spring 컨테이너에 등록 ss: SpringSecurity
public class PermissionService {
    /**
     * 모든 권한 표시
     */
    private static final String ALL_PERMISSION = "*:*:*";


    @Autowired
    private TokenService tokenService;

    /**
     * 사용자 지정한 권한이 있는지 확인
     *
     * @param permission 권한 문자열
     * @return 사용자 권한 있는지 여부
     */
    public boolean hasPermi(String permission) {
        if (StringUtils.isEmpty(permission)) { //권한 문자열 비어 있으면 false로 반납
            return false;
        }
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());//사용자 로그인 정보를 얻기

        //로그인 사용자 비어 있거나 사용자의 권한 목록이 비어 있으면 사용자가 로그인 하지 않거나 권한 없다고 false로 반납
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        return hasPermissions(loginUser.getPermissions(), permission);//권한 목록에 지정한 권한 문자열이 있는지 확인
    }


    /**
     * 권한 포함하는지 확인
     *
     * @param permissions 사용자 권한 목록
     * @param permission  권한 문자열
     * @return 사용자 어떤 권한 있는지 확인
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        //사용자 권한 목록중에 모든 권한 있거나 권한 목록중에 지정 권한 문자열을 포함되어 있으면 true로 반납
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }
}
