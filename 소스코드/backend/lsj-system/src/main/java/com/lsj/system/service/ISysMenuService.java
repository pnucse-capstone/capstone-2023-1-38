package com.lsj.system.service;

import java.util.List;
import java.util.Set;

import com.lsj.common.core.domain.TreeSelect;
import com.lsj.common.core.domain.entity.SysMenu;
import com.lsj.system.domain.vo.RouterVo;

/**
 * 메뉴 업무층
 */
public interface ISysMenuService {
    /**
     * 사용자 따라 시스템 메뉴 목록 조회
     * @param userId 사용자 ID
     * @return 메뉴 목록
     */
    public List<SysMenu> selectMenuList(Long userId);

    /**
     * 사용자 따라 시스템 메뉴 목록 조회
     *
     * @param menu   메뉴 정보
     * @param userId 사용자 ID
     * @return 메뉴 목록
     */
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId);

    /**
     * 사용자 ID로 권한 조회
     *
     * @param userId 用户ID
     * @return 권한 목록
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 사용자 ID로 메뉴트리 정보 조회
     *
     * @param userId 사용자 ID
     * @return 메뉴 목록
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 역할 ID로 메뉴트리 정보 조회
     *
     * @param roleId 사용자 ID
     * @return 메뉴 목록 선택
     */
    public List<Integer> selectMenuListByRoleId(Long roleId);

    /**
     * 프런트엔드 라우팅에 필요한 메뉴 구축
     *
     * @param menus 메뉴 목록
     * @return route 목록
     */
    public List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * 프런트 엔드에 필요한 트리 구조 구축
     *
     * @param menus 메뉴 목록
     * @return 트리 구조 목록
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * 프런트 엔드에 필요한 드롭다운 트리 구조 구축
     *
     * @param menus 메뉴 목록
     * @return 드롭다운 트리 구조 목록
     */
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * 메뉴 ID로 메뉴 정보를 조회
     *
     * @param menuId 메뉴 ID
     * @return 메뉴 정보
     */
    public SysMenu selectMenuById(Long menuId);

    /**
     * 메뉴 자식 노드가 있는지 여부
     *
     * @param menuId 메뉴 ID
     * @return 결과 true 존재 false 존재하지 않음
     */
    public boolean hasChildByMenuId(Long menuId);

    /**
     * 메뉴에 해당 역할이 있는지 조회
     *
     * @param menuId 메뉴 ID
     * @return 결과 true 존재 false 존재하지 않음
     */
    public boolean checkMenuExistRole(Long menuId);

    /**
     * 메뉴 정보 추가
     *
     * @param menu 메뉴 정보
     * @return 결과
     */
    public int insertMenu(SysMenu menu);

    /**
     * 메뉴 정보 수정
     *
     * @param menu 메뉴 정보
     * @return 결과
     */
    public int updateMenu(SysMenu menu);

    /**
     * 메뉴 관리 정보 삭제
     *
     * @param menuId 메뉴 ID
     * @return 결과
     */
    public int deleteMenuById(Long menuId);

    /**
     * 메뉴 이름은 유일한 지 체크
     *
     * @param menu 메뉴 정보
     * @return 결과
     */
    public String checkMenuNameUnique(SysMenu menu);
}
