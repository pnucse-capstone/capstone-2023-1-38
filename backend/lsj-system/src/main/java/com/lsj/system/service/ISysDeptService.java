package com.lsj.system.service;

import java.util.List;

import com.lsj.common.core.domain.TreeSelect;
import com.lsj.common.core.domain.entity.SysDept;

/**
 * 부서 관리 업무층
 */
public interface ISysDeptService {
    /**
     * 부서 관리 데이터를 조회
     *
     * @param dept 부서 정보
     * @return 부서 정보 집합
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 프런트 엔드에 필요한 트리 구조 구축
     * @param depts 부서 목록
     * @return 트리 구조 목록
     */
    public List<SysDept> buildDeptTree(List<SysDept> depts);


    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts);

    /**
     * 역할 ID로 부서 트리 정보를 조회
     *
     * @param roleId 역할 ID
     * @return 부서 목록
     */
    public List<Integer> selectDeptListByRoleId(Long roleId);

    /**
     * 부서 ID로 정보를 조회
     * @param deptId 부서 ID
     * @return 부서 정보
     */
    public SysDept selectDeptById(Long deptId);

    /**
     * ID로 모든 하위 부서 조회(정상 상태)
     * @param deptId 부서 ID
     * @return 하위 부서 수
     */
    public int selectNormalChildrenDeptById(Long deptId);

    /**
     * 부서 하위 노드가 있는지 확인
     *
     * @param deptId 부서 ID
     * @return 결과
     */
    public boolean hasChildByDeptId(Long deptId);

    /**
     * 부서에 해당 사용자 존재하는지 조회
     *
     * @param deptId 부서 ID
     * @return 결과 true 존재 false 존재하지 않음
     */
    public boolean checkDeptExistUser(Long deptId);

    /**
     * 부서 이름은 유일한지 체크
     *
     * @param dept 부서 정보
     * @return 결과
     */
    public String checkDeptNameUnique(SysDept dept);

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
     * 부서 관리 정보를 삭제
     *
     * @param deptId 부서 ID
     * @return 결과
     */
    public int deleteDeptById(Long deptId);
}
