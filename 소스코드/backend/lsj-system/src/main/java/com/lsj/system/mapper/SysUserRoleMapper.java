package com.lsj.system.mapper;

import java.util.List;

import com.lsj.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 사용자 및 역할 관련표 데이터 층
 */
@Repository
public interface SysUserRoleMapper {
    /**
     * 사용자 ID로 사용자 및 역할 관련
     *
     * @param userId 사용자 ID
     * @return 결과
     */
    public int deleteUserRoleByUserId(Long userId);

    /**
     * 여러 사용자 및 역할 관련을 삭제
     *
     * @param ids 삭제할 데이터 ID
     * @return 결과
     */
    public int deleteUserRole(Long[] ids);

    /**
     * 역할 ID로 사용자 사용량을 조회
     *
     * @param roleId 역할ID
     * @return 결과
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * 여러 사용사 역할 정보를 추가
     *
     * @param userRoleList 사용자 역할 목록
     * @return 결과
     */
    public int batchUserRole(List<SysUserRole> userRoleList);

    /**
     * 사용자 및 역할 관련 정보를 삭제
     *
     * @param userRole 사용자 및 역할 관련 정보
     * @return 결과
     */
    public int deleteUserRoleInfo(SysUserRole userRole);

    /**
     * 여러 사용자 역할 수권 취소
     * 批量取消授权用户角色
     *
     * @param roleId  역할 ID
     * @param userIds 삭제할 사용자 데이터 ID
     * @return 결과
     */
    public int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}
