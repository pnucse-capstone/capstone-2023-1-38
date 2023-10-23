package com.lsj.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lsj.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lsj.common.annotation.DataScope;
import com.lsj.common.constant.UserConstants;
import com.lsj.common.core.domain.entity.SysRole;
import com.lsj.common.exception.CustomException;
import com.lsj.common.utils.StringUtils;
import com.lsj.common.utils.spring.SpringUtils;
import com.lsj.system.domain.SysRoleDept;
import com.lsj.system.domain.SysRoleMenu;
import com.lsj.system.mapper.SysRoleDeptMapper;
import com.lsj.system.mapper.SysRoleMapper;
import com.lsj.system.mapper.SysRoleMenuMapper;
import com.lsj.system.mapper.SysUserRoleMapper;

/**
 * 역할 업무층 처리
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleDeptMapper roleDeptMapper;

    /**
     * 조건에 따라 페이지별로 역할 데이터 조회
     *
     * @param role 역할 정보
     * @return 역할 데이터 집합 정보
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role);
    }

    /**
     * 사용자 ID로 역할을 조회
     *
     * @param userId 사용자 ID
     * @return 권한 목록
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 모든 역할을 조회
     *
     * @return 역할 목록
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return SpringUtils.getAopProxy(this).selectRoleList(new SysRole());
    }

    /**
     * 사용자 ID로 역할 선택 옵션 목록을 얻기
     *
     * @param userId 사용자 ID
     * @return 역할 ID 목록
     */
    @Override
    public List<Integer> selectRoleListByUserId(Long userId) {
        return roleMapper.selectRoleListByUserId(userId);
    }

    /**
     * 역할 ID로 역할을 조회
     *
     * @param roleId 역할 ID
     * @return 역할 객체 정보
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * 사용자 이름 유일한지 체크
     *
     * @param role 역할 정보
     * @return 결과
     */
    @Override
    public String checkRoleNameUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();//역할 ID를 얻기, null이면 -1L

        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());//역할 이름 같은 역할 정보를 조회
        //조회 결과가 null아니고 역할 ID 다르면 해당 역할 이름은 존재
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;//유일한지 않으로 반납
        }
        return UserConstants.UNIQUE;//유일함으로 반납
    }

    /**
     * 역할 권한 유일한지 체크
     *
     * @param role 역할 정보
     * @return 결과
     */
    @Override
    public String checkRoleKeyUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();//역할 ID를 얻기, null이면 -1L
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());//역할 권한 같은 역할 정보를 조회
        //조회 결과 null아니고 역할 ID가 현재 역할 ID와 다르면 해당 역할 이름은 존재함
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;//유일한지 않으로 반납
        }
        return UserConstants.UNIQUE;//유일함으로 반납
    }

    /**
     * 역할이 조작을 허용하는지 체크
     *
     * @param role 역할 정보
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) { //역할 null아니고 역할은 관리자이면 에외발생
            throw new CustomException("슈퍼 관리자 역할을 조작할 수 없습니다");
        }
    }

    /**
     * 역할 ID로 역할 사용량를 조회
     *
     * @param roleId 역할 ID
     * @return 결과
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 역할 정보를 추가
     *
     * @param role 역할 정보
     * @return 결과
     */
    @Override
    @Transactional
    public int insertRole(SysRole role) {
        // 역할 정보를 추가
        roleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     * 역할 정보 수정
     *
     * @param role 역할 정보
     * @return 결과
     */
    @Override
    @Transactional
    public int updateRole(SysRole role) {
        // 역할 정보를 수정
        roleMapper.updateRole(role);
        // 메뉴와 관련된 역할을 삭제
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * 역할 상태 수정
     *
     * @param role 역할 정보
     * @return 결과
     */
    @Override
    public int updateRoleStatus(SysRole role) {
        return roleMapper.updateRole(role);
    }

    /**
     * 데이터 권한 정보를 수정
     *
     * @param role 역할 정보
     * @return 결과
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role) {
        // 역할 정보를 수정
        roleMapper.updateRole(role);
        // 부서와 관련된 역할 삭제
        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
        // 역할 및 부서 정보 추가(데이터 권한)
        return insertRoleDept(role);
    }

    /**
     * 역할 메뉴 정보를 추가
     *
     * @param role 역할 객체
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;//영향 행수 초기화
        // 사용자와 역할 관리 추가
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();//역할과 메뉴의 관련 정보
        for (Long menuId : role.getMenuIds()) { //역할의 각 메뉴 방문
            SysRoleMenu rm = new SysRoleMenu();//SysRoleMenu 객체를 생성
            rm.setRoleId(role.getRoleId());//역할 ID 설정
            rm.setMenuId(menuId);//메뉴 ID 설정
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = roleMenuMapper.batchRoleMenu(list);//여러 역할 메뉴 관련 정보를 추가하고 영향 미친 행수 반납
        }
        return rows;
    }

    /**
     * 역할 부서 정보 추가(데이터 권한)
     *
     * @param role 사용자 객체
     */
    public int insertRoleDept(SysRole role) {
        int rows = 1;
        // 역할 및 부서(데이터 권한) 관리 추가
        List<SysRoleDept> list = new ArrayList<SysRoleDept>();
        for (Long deptId : role.getDeptIds()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0) {
            rows = roleDeptMapper.batchRoleDept(list);
        }
        return rows;
    }

    /**
     * 역할 ID로 역할 삭제
     *
     * @param roleId 역할 ID
     * @return 결과
     */
    @Override
    public int deleteRoleById(Long roleId) {
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * 여러 역할 정보를 삭제
     *
     * @param roleIds 삭제할 역할 ID
     * @return 결과
     */
    @Override
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRole(roleId));//각 역할을 수정할 수 있는지 체크(관리자 역할을 수정할 수 없다)
            SysRole role = selectRoleById(roleId);//열할 IDfh 각 역할 정보를 얻기
            if (countUserRoleByRoleId(roleId) > 0) { //각 역할 사용량을 조회하고 있으면 경고 메시지 발행
                throw new CustomException(String.format("%1$s분배 되었으니 삭제불가합니다", role.getRoleName()));//%1$s已分配,不能删除
            }
        }
        return roleMapper.deleteRoleByIds(roleIds);//역할 목록을 삭제
    }
}
