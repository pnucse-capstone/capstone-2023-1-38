package com.lsj.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.lsj.common.core.domain.entity.SysDept;

/**
 * 부서 관리 데이터층
 */
public interface SysDeptMapper {
    /**
     * 부서 관리 데이터를 조회
     *
     * @param dept 부서 정보
     * @return 부서 정보 집합
     */
    public List<SysDept> selectDeptList(SysDept dept);


    public List<Integer> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);

    /**
     * 부서 ID로 정보를 조회
     *
     * @param deptId 부서 ID
     * @return 부서 정보
     */
    public SysDept selectDeptById(Long deptId);

    /**
     * ID로 모든 자식 부서를 조회
     *
     * @param deptId 부서 ID
     * @return 부서 목록
     */
    public List<SysDept> selectChildrenDeptById(Long deptId);

    /**
     * ID로 모든 하위 부서 조회(정상 상태)
     * @param deptId 부서 ID
     * @return 하위 부서 수
     */
    public int selectNormalChildrenDeptById(Long deptId);

    /**
     * 하위 노드 있는지 확인
     *
     * @param deptId 부서 ID
     * @return 결과
     */
    public int hasChildByDeptId(Long deptId);

    /**
     * 부서에 해당 사용자 존재하는지 조회
     *
     * @param deptId 부서 ID
     * @return 결과
     */
    public int checkDeptExistUser(Long deptId);

    /**
     * 부서 이름 유일한지 체크
     *
     * @param deptName 부서 이름
     * @param parentId 부모 부서 ID
     * @return 결과
     */
    public SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);

    /**
     * 부서 정보를 추가
     *
     * @param dept 부서 정보
     * @return 결과
     */
    public int insertDept(SysDept dept);

    /**
     * 부서 정보를 수정
     *
     * @param dept 부서 정보
     * @return 결과
     */
    public int updateDept(SysDept dept);

    /**
     * 부서의 상위 부서 상태 수정
     *
     * @param dept 부서
     */
    public void updateDeptStatus(SysDept dept);

    /**
     * 하위 요소 관계 수정
     * @param depts 하위 요소
     * @return 결과
     */
    public int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 부서 정보를 삭제
     *
     * @param deptId 부서 ID
     * @return 결과
     */
    public int deleteDeptById(Long deptId);

    List<SysDept> getFactoryByDeptId(Integer deptId);
}
