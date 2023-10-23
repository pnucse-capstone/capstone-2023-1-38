package com.lsj.system.mapper;

import java.util.List;

import com.lsj.common.core.domain.entity.SysRole;
import org.springframework.stereotype.Repository;

/**
 * 角色表 数据层
 * 역할 테이블  데이터 층
 */
@Repository
public interface SysRoleMapper {
    /**
     * 조건에 따라 페이지별로 역할 데이터 조회
     *
     * @param role 역할 정보
     * @return 역할 데이터 집합 정보
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * 사용자 ID로 역할 권한 집합 조회
     *
     * @param userId 사용자 ID
     * @return 역할 목록
     */
    public List<SysRole> selectRolePermissionByUserId(Long userId);

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
     * 사용자 ID로 역할 조회
     *
     * @param userName 사용자 이름
     * @return 역할 목록
     */
    public List<SysRole> selectRolesByUserName(String userName);

    /**
     * 역할 이름 유일한지 체크
     *
     * @param roleName 역할 이름
     * @return 역할 정보
     */
    public SysRole checkRoleNameUnique(String roleName);

    /**
     * 역할 권한 유일한지 체크
     *
     * @param roleKey 역할 권한
     * @return 역할 정보
     */
    public SysRole checkRoleKeyUnique(String roleKey);

    /**
     * 역할 정보 수정
     *
     * @param role 역할 정보
     * @return 결과
     */
    public int updateRole(SysRole role);

    /**
     * 역할 정보를 추가
     *
     * @param role 역할 정보
     * @return 결과
     */
    public int insertRole(SysRole role);

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
