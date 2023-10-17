package com.lsj.framework.web.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.lsj.common.core.domain.entity.SysUser;
import com.lsj.system.service.ISysMenuService;
import com.lsj.system.service.ISysRoleService;

/**
 * 사용자 권한 처리
 */
@Component
public class  SysPermissionService {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    /**
     * 역할 데이터 권한을 얻기
     *
     * @param user 사용자 정보
     * @return 역할 권한 정보
     */
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<String>();
        // 관리자는 모든 권한 가짐
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));//사용자 ID로 역할 권한 집합을 얻어서 roles집합에 추가
        }
        return roles;
    }

    /**
     * 메뉴 데이터 권한을 얻기
     *
     * @param user 사용자 정보
     * @return 메뉴 권한 정보
     */
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<String>();
        // 관리자 모든 권한 가짐
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else {
            //관리자아니면 userID로 조회
            perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));//사용자 ID로 메뉴 권한 집합을 얻어서 perms집합에 추가
        }
        return perms;
    }
}
