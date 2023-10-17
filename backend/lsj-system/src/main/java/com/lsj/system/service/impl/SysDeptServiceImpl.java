package com.lsj.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.lsj.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lsj.common.annotation.DataScope;
import com.lsj.common.constant.UserConstants;
import com.lsj.common.core.domain.TreeSelect;
import com.lsj.common.core.domain.entity.SysDept;
import com.lsj.common.core.domain.entity.SysRole;
import com.lsj.common.exception.CustomException;
import com.lsj.common.utils.StringUtils;
import com.lsj.system.mapper.SysDeptMapper;
import com.lsj.system.mapper.SysRoleMapper;

/**
 * 부서 관리 service implementation
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {
    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * 부서 관리 데이터를 조회
     *
     * @param dept 부서 정보
     * @return 부서 정보 집합
     */
    @Override
    @DataScope(deptAlias = "d")//데이터 범위 제어
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 프런트 엔드에 필요한 트리 구조 구축
     *
     * @param depts 부서 목록
     * @return 트리 구조 목록
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<SysDept>();//하위 노드 갗추는 상위 노드를 저장
        List<Long> tempList = new ArrayList<Long>();//부서 목록의 모든 자식 부서의 deptId를 저장
        for (SysDept dept : depts) {
            tempList.add(dept.getDeptId());//각 부서의 ID를 tempList에 저장
        }
        for (Iterator<SysDept> iterator = depts.iterator(); iterator.hasNext(); ) {
            //depts.iterator(): depts iterator 생성
            //iterator.hasNext(): next element있는지 확인
            SysDept dept = (SysDept) iterator.next();//iterator에 다음 element 반납
            //최상위 노드인 경우 상위 노드의 모든 하위 노드를 방문
            if (!tempList.contains(dept.getParentId())) { //현재 dept 객체의 상위 노드가 tempList에 포함되어 있는지 확인(없으면 최종 노드)
                /*상위 노드인 경우 상위 노드의 모든 자식 노드를 recursion하고 재귀 함수 recursionFn을 호출하여
                매개 변수 부서와 현재 부서 객체 dept를 전달하여 부서 객체의 자식 노드를 처리한다.*/
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 프런트 엔드에 필요한 드롭다운 트리 구조 구축
     *
     * @param depts 부서 목록
     * @return 드롭다운 트리 구조 목록
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);//하위 노드 갗추는 상위 노드 list
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
        //deptTrees.stream(): deptTrees를 스트림으로 변환(deptTrees 원소들 순차적으로 처리)
        //map(TreeSelect::new): 각 요소에 대해 TreeSelect 객체를 생성
        //collect(Collectors.toList(): 스트림의 요소들을 리스트로 수집
    }

    /**
     * 역할 ID로 부서 트리 정보를 조회
     *
     * @param roleId 역할 ID
     * @return 부서 목록
     */
    @Override
    public List<Integer> selectDeptListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return deptMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 부서 ID로 정보를 조회
     *
     * @param deptId 부서 ID
     * @return 부서 정보
     */
    @Override
    public SysDept selectDeptById(Long deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * ID로 모든 하위 부서 조회(정상 상태)
     *
     * @param deptId 부서 ID
     * @return 하위 부서 수
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 하위 노드 있는지 확인
     *
     * @param deptId 부서 ID
     * @return 결과
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 부서에 해당 사용자 존재하는지 조회
     *
     * @param deptId 부서 ID
     * @return 결과 true 존재 false 존재하지 않음
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 메뉴 이름은 유일한 지 체크
     *
     * @param dept 부서 정보
     * @return 결과
     */
    @Override
    public String checkDeptNameUnique(SysDept dept) {
        Long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();//부서 ID를 얻기, null이면 -1L
        //지정한 부서 이름과 같은 상위 노드 정보를 조회(없으면 null)
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        //조회 결과 null아니고 부서 ID가 현재 부서 ID 다르면 해당 메뉴 이름은 존재함
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
            return UserConstants.NOT_UNIQUE;//유일한지 않음으로 반납
        }
        return UserConstants.UNIQUE;//유일함으로 반납
    }

    /**
     * 부서 정보 추가
     *
     * @param dept 부서 정보
     * @return 결과
     */
    @Override
    public int insertDept(SysDept dept) {
        SysDept info = deptMapper.selectDeptById(dept.getParentId());
        // 상이 노드 정상 상태아니면 자식 노드 추가 불가
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new CustomException("정지된 부서, 추가 불가합니다");//部门停用，不允许新增
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());//조상 목록을 현재의 조상 목록과 상위 부서 Id로 설정
        return deptMapper.insertDept(dept);//부서를 사입
    }

    /**
     * 부서 정보를 수정
     *
     * @param dept 부서 정보
     * @return 결과
     */
    @Override
    public int updateDept(SysDept dept) {
        SysDept newParentDept = deptMapper.selectDeptById(dept.getParentId());//상위 부서 객체를 얻기
        SysDept oldDept = deptMapper.selectDeptById(dept.getDeptId());//ID로 부서 객체를 얻기

        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) { //newParentDept 와 oldDept 존재하면
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();//새로운 ancestors
            String oldAncestors = oldDept.getAncestors();//원래의 ancestors를 얻기
            dept.setAncestors(newAncestors);//현재 부서의 ancestors를 새로운 ancestors로 설정
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
            updateParentDeptStatus(dept);
        }
        return result;
    }

    /**
     * 부서의 상위 부서 상태 수정
     *
     * @param dept 현재 부서
     */
    private void updateParentDeptStatus(SysDept dept) {
        String updateBy = dept.getUpdateBy();
        dept = deptMapper.selectDeptById(dept.getDeptId());
        dept.setUpdateBy(updateBy);
        deptMapper.updateDeptStatus(dept);
    }

    /**
     * 하위 요소 관계 수정
     *
     * @param deptId       수정된 부서 ID
     * @param newAncestors 새로운 부모 ID 집합
     * @param oldAncestors 이전의 부모 ID 집합
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);//ID로 부서의 자식 부서 목록을 조회
        for (SysDept child : children) {
            //oldAncestors를 새 조상 부서 경로 newAncestors로 바꿔서 각 child 객체의 ancestors 설정
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0) { //자식 부서 있으면 자식 부서의 정보를 일괄 업데이트
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 부서 관리 정보를 삭제
     *
     * @param deptId 부서 ID
     * @return 결과
     */
    @Override
    public int deleteDeptById(Long deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * recursion list
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        List<SysDept> childList = getChildList(list, t);//자식 노드 목록을 얻기
        t.setChildren(childList);//자식 노드 childList를 부서 객체 t의 자식 노드로 설정
        for (SysDept tChild : childList) { //childList의 각 요소를 방문
            if (hasChild(list, tChild)) { //자식 노드 tChild가 자식 노드 더 있는 지 확인
                recursionFn(list, tChild);//있으면 recursionFn메소드 재 호출
            }
        }
    }

    /**
     * 자식 목록을 얻기
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<SysDept>();////자식 노드를 저장하는 list 생성
        Iterator<SysDept> it = list.iterator();//부서 list의 iterator 생성
        while (it.hasNext()) { //각 부서 객체를 방문
            SysDept n = (SysDept) it.next();//현재 방문된 부서 객체를 얻기

            //n의 상위 노드 ID가 null 아니며 t의 ID와 일치하면 tlist배열에 추가
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);//n를 자식 노드 모록 tlist에 저장
            }
        }
        return tlist;
    }

    /**
     * 자식 노드가 있는지 확인
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
