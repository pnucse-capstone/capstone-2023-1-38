package com.lsj.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.lsj.common.core.domain.entity.SysMenu;

/**
 * 메뉴 테이블 데이터 층
 */
public interface SysMenuMapper {
    /**
     * 시스템 메뉴 목록 조회
     *
     * @param menu 메뉴 정보
     * @return 메뉴 목록
     */
    public List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * 사용자 권한 조회
     *
     * @return 권한 목록
     */
    public List<String> selectMenuPerms();

    /**
     * 사용자 따라 시스템 메뉴 목록을 조회
     *
     * @param menu 메뉴 정보
     * @return 메뉴 목록
     */
    public List<SysMenu> selectMenuListByUserId(SysMenu menu);

    /**
     * 사용자 ID로 메뉴 권한 조회
     *
     * @param userId 사용자 ID
     * @return 권한 목록
     */
    public List<String> selectMenuPermsByUserId(Long userId);

    /**
     * 사용자 ID로 메뉴트리 조회
     *
     * @return 모든 메뉴 목록
     */
    public List<SysMenu> selectMenuTreeAll();

    /**
     * 사용자 ID로 메뉴트리 조회
     *
     * @param userId 사용자 ID
     * @return 메뉴 목록
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 역할 ID로 메뉴트리 정보 조회
     *
     * @param roleId            역할 ID
     * @param menuCheckStrictly 메뉴트리 선택항 관련 여부 표시
     * @return 메뉴 목록
     */
    public List<Integer> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

    /**
     * 메뉴 ID로 메뉴 정보를 조회
     *
     * @param menuId 메뉴 ID
     * @return 메뉴 정보
     */
    public SysMenu selectMenuById(Long menuId);

    /**
     * 메뉴 자식 노드 존재하는지 여부
     *
     * @param menuId 메뉴 ID
     * @return 결과
     */
    public int hasChildByMenuId(Long menuId);

    /**
     * 메뉴 정보 추가
     *
     * @param menu 메뉴 정보
     * @return 결과
     */
    public int insertMenu(SysMenu menu);

    /**
     * 메뉴 정보 수정
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
     * 메뉴 이름은 유일한지 체크
     *
     * @param menuName 메뉴 이름
     * @param parentId 상위 메뉴 ID
     * @return 결과
     */
    public SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);
}
