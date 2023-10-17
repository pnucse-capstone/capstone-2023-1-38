package com.lsj.system.mapper;

import java.util.List;

import com.lsj.system.domain.SysRoleDept;

/**
 * 역할 및 부서 관련표  데이터 층
 */
public interface SysRoleDeptMapper {
    /**
     * 역할 ID로 역할 및 부서 관련을 삭제
     *
     * @param roleId 역할 ID
     * @return 결과
     */
    public int deleteRoleDeptByRoleId(Long roleId);

    /**
     * 여러 역할 및 부서 관련 정보를 삭제
     *
     * @param ids 사젝할 데이터 ID
     * @return 결과
     */
    public int deleteRoleDept(Long[] ids);

    /**
     * 부서 사용량 조회
     *
     * @param deptId 부서 ID
     * @return 결과
     */
    public int selectCountRoleDeptByDeptId(Long deptId);

    /**
     * 여러 역할 및 부서 정보를 추가
     *
     * @param roleDeptList 역할 및 부서 목록
     * @return 결과
     */
    public int batchRoleDept(List<SysRoleDept> roleDeptList);
}
