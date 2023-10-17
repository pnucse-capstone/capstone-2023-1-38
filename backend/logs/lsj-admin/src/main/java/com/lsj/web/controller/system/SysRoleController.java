package com.lsj.web.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lsj.common.constant.UserConstants;
import com.lsj.common.core.controller.BaseController;
import com.lsj.common.core.domain.AjaxResult;
import com.lsj.common.core.domain.entity.SysRole;
import com.lsj.common.core.domain.model.LoginUser;
import com.lsj.common.core.page.TableDataInfo;
import com.lsj.common.utils.SecurityUtils;
import com.lsj.common.utils.ServletUtils;
import com.lsj.common.utils.StringUtils;
import com.lsj.framework.web.service.SysPermissionService;
import com.lsj.framework.web.service.TokenService;
import com.lsj.system.service.ISysRoleService;
import com.lsj.system.service.ISysUserService;

/**
 * 역할 정보
 */
@RestController //HTTP 요청을 처리하며 데이터 응답을 반납
@RequestMapping("/system/role") //HTTP 요청은 Controller 클래스에 mapping하고 Controller에서 처리하는 URL prefix를 지정함
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService userService;

    /**
     * 조건에 따라 페이지별로 역할 데이터 조회
     *
     * @param role 역할 정보
     * @return 역할 데이터 집합 정보
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")//사용자 권한 체크
    @GetMapping("/list")//메소드는 get 요청 처리
    public TableDataInfo list(SysRole role) {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);//역할 데이터 목록를 얻기
        return getDataTable(list);//역할 목록 따라 데이터 정보를 얻기
    }


    /**
     * 역할 ID로 정보를 얻기
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")//사용자 권한 체크
    @GetMapping(value = "/{roleId}")//메소드는 get 동적 요청을 처리
    public AjaxResult getInfo(@PathVariable Long roleId) { //@PathVariable:URL 경로의 변수 값을 해당 메서드 매개변수에 바인딩
        return AjaxResult.success(roleService.selectRoleById(roleId));//역할 ID로 열할을 조회하고 응답 메시지를 반납
    }

    /**
     * 역할 추가
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")//사용자 권한 체크
    @PostMapping//메소드는 post 요청 처리
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        //@Validated:role 매개변수 대해 데이터 체크

        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) { //역할 이름 유일하지 않으면
            return AjaxResult.error("역할 추가'" + role.getRoleName() + "'실패, 역할 이름 존재");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) { //역할 권한 유일하지 않으면
            return AjaxResult.error("역할 추가'" + role.getRoleName() + "'실패, 역할 권한 존재");
        }
        role.setCreateBy(SecurityUtils.getUsername());//역할 생성자를 현재 로그인한 사용자로 설정
        return toAjax(roleService.insertRole(role));//역할을 추가하고 응답 메시지 반환
    }


    /**
     * 역할 수정
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")//사용자 권한 체크
    @PutMapping//메소드는 put 요청을 처리
    public AjaxResult edit(@Validated @RequestBody SysRole role) { //@Validated:role 매개변수 대해 데이터 체크

        roleService.checkRoleAllowed(role);//역할을 수정할 수 있는지 체크(관리자 역할을 수정할 수 없다)
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) { //역할 이름 유일하지 않으면
            return AjaxResult.error("역할 수정'" + role.getRoleName() + "'실패, 역할 이름 존재");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) { //역할 권한 유일하지 않으면
            return AjaxResult.error("역할 수정'" + role.getRoleName() + "'실패, 역할 권한 존재");
        }
        role.setUpdateBy(SecurityUtils.getUsername());//현재 로그인한 사용자 이름얻어서 역할의 업데이트자로 설정

        if (roleService.updateRole(role) > 0) { //역할 정보를 수정하고 영향 행수 있는제 판단
            // 캐시된 사용자 권한 업데이트
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());//사용자 로그인 정보를 얻기
            //사용자가 null아니며 관리자 아니면
            if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin()) {
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));//사용자 메뉴 권한을 재설정
                loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));//으로 사용자 정보를 재설정
                tokenService.setLoginUser(loginUser);//업데이트한 loginUser 객체를 재설정
            }
            return AjaxResult.success();
        }
        return AjaxResult.error("역할 수정'" + role.getRoleName() + "'실패, 관리자에게 연락하세요");
    }

    /**
     * 데이터 권한 수정
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")//사용자 권한 체크
    @PutMapping("/dataScope")//메소드는 PUT {dataScope}동적 요청을 처리
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);//역할을 수정할 수 있는지 체크(관리자 역할을 수정할 수 없다)
        return toAjax(roleService.authDataScope(role));//권한 정보를 수정하고 응답 메시지를 반납
    }

    /**
     * 상태 수정
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")//사용자 권한 체크
    @PutMapping("/changeStatus")//메소드는 PUT {changeStatus}동적 요청을 처리
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);//역할을 수정할 수 있는지 체크(관리자 역할을 수정할 수 없다)
        role.setUpdateBy(SecurityUtils.getUsername());//로그인한 사용자 이름으로 업데이트자를 수정
        return toAjax(roleService.updateRoleStatus(role));//역할의 상태를 수정하고 응답 메시지 반납
    }

    /**
     * 역할 삭제
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")//사용자 권한 체크
    @DeleteMapping("/{roleIds}")//메소드는 DELETE {roleIds}동적 요청을 처리
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 역할 선택 옵션 목록을 얻기
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")//메소드는 get 동적 요청을 처리
    public AjaxResult optionselect() {
        return AjaxResult.success(roleService.selectRoleAll());
    }
}




