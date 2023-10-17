package com.lsj.system.service;

import java.util.List;
import java.util.Set;

import com.lsj.common.core.domain.entity.SysRole;

/**
 * 역할 업무층
 */
public interface ISysRoleService {
    /**
     * 조건에 따라 페이지별로 역할 데이터 조회
     *
     * @param role 역할 정보
     * @return 역할 데이터 집합 정보
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * 사용자 ID로 역할을 조회
     *
     * @param userId 사용자 ID
     * @return 권한 목록
     */
    public Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * 모든 역할을 조회
     *
     * @return 역할 목록
     */
    public List<SysRole> selectRoleAll();

    /**
     * 사용자 ID로 역할 선택 옵션 목록을 얻기
     *
     * @param userId 사용자 ID
     * @return 역할 ID 목록
     */
    public List<Integer> selectRoleListByUserId(Long userId);

    /**
     * 역할 ID로 역할을 조회
     *
     * @param roleId 역할 ID
     * @return 역할 객체 정보
     */
    public SysRole selectRoleById(Long roleId);

    /**
     * 사용자 이름 유일한지 체크
     *
     * @param role 역할 정보
     * @return 결과
     */
    public String checkRoleNameUnique(SysRole role);

    /**
     * 역할 권한 유일한지 체크
     *
     * @param role 역할 정보
     * @return 결과
     */
    public String checkRoleKeyUnique(SysRole role);

    /**
     * 역할이 조작을 허용하는지 체크
     *
     * @param role 역할 정보
     */
    public void checkRoleAllowed(SysRole role);

    /**
     * 역할 ID로 역할 사용량를 조회
     *
     * @param roleId 역할 ID
     * @return 결과
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * 역할 정보를 추가
     *
     * @param role 역할 정보
     * @return 결과
     */
    public int insertRole(SysRole role);

    /**
     * 역할 정보 수정
     *
     * @param role 역할 정보
     * @return 결과
     */
    public int updateRole(SysRole role);

    /**
     * 역할 상태 수정
     *
     * @param role 역할 정보
     * @return 결과
     */
    public int updateRoleStatus(SysRole role);

    /**
     * 데이터 권한 정보를 수정
     *
     * @param role 역할 정보
     * @return 결과
     */
    public int authDataScope(SysRole role);

    /**
     * 역할 ID로 역할을 삭제
     *
     * @param roleId 역할 ID
     * @return 결과
     */
    public int deleteRoleById(Long roleId);

    /**
     * 여러 역할 정보를 삭제
     *
     * @param roleIds 삭제할 역할 ID
     * @return 결과
     */
    public int deleteRoleByIds(Long[] roleIds);
}
