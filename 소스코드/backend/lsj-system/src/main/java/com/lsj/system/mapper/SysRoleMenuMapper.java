package com.lsj.system.mapper;

import java.util.List;

import com.lsj.system.domain.SysRoleMenu;

/**
 * 역할 및 메뉴 관련표  데이터 층
 */
public interface SysRoleMenuMapper {
    /**
     * 메뉴 사용량 조회
     *
     * @param menuId 메뉴 ID
     * @return 결과
     */
    public int checkMenuExistRole(Long menuId);

    /**
     * 역할 ID로 역할 및 메뉴 관련을 삭제
     *
     * @param roleId 역할 ID
     * @return 결과
     */
    public int deleteRoleMenuByRoleId(Long roleId);

    /**
     * 여러 역할 메뉴 정보 추가
     *
     * @param roleMenuList 역할 메뉴 목록
     * @return 결과
     */
    public int batchRoleMenu(List<SysRoleMenu> roleMenuList);
}
