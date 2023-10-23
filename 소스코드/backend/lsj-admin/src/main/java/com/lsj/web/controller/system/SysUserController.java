package com.lsj.web.controller.system;

import java.util.List;
import java.util.stream.Collectors;

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
import com.lsj.common.core.domain.entity.SysUser;
import com.lsj.common.core.page.TableDataInfo;
import com.lsj.common.utils.SecurityUtils;
import com.lsj.common.utils.StringUtils;
import com.lsj.framework.web.service.TokenService;
import com.lsj.system.service.ISysPostService;
import com.lsj.system.service.ISysRoleService;
import com.lsj.system.service.ISysUserService;

/**
 * 사용자 정보
 */
@RestController //HTTP 요청을 처리하며 데이터 응답을 반납
@RequestMapping("/system/user")//컨트롤러 메소드는"/system/user"의 요청 경로를 처리
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private TokenService tokenService;

    /**
     * 사용자 목록 얻기
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")//사용자 권한 체크
    @GetMapping("/list")//메소드는 get 요청을 처리
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);//사용자 정보 집합을 조회
        return getDataTable(list);//사용자 정보 집합으로 정보 데이터를 얻기
    }


    /**
     * 사용자 ID로 정보를 얻기
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")//사용자 권한 체크
    @GetMapping(value = {"/", "/{userId}"})//메소드는 get 동적 요청을 처리(userId 없으면 모든 Id 조회// )
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        AjaxResult ajax = AjaxResult.success();//성공 응답 객체 생성
        List<SysRole> roles = roleService.selectRoleAll();//모든 역할 목록을 얻기
        //userId로 사용자가 관리자인지 판단하고 true이면 모든 역할 목록 속성값에 넣다, false이면 stream()로 관리자 역할 필터링하고 list 로 변환
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());//모든 직위 목록 얻어서 posts 속성의 속성값으로 넣기
        if (StringUtils.isNotNull(userId)) {
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));//ID로 사용자 정보를 얻어서 DATA_TAG 속성의 속성값으로 넣기
            ajax.put("postIds", postService.selectPostListByUserId(userId));//ID로 사용자 직위 목록을 얻어서 postIds 속성의 속성값으로 넣기
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));//ID로 사용자 역할 목록을 얻어서 roleIds 속성의 속성값으로 넣기
        }
        return ajax;
    }

    /**
     * 사용자 추가
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")//사용자 권한 체크
    @PostMapping//메소드는 post 요청을 처리
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) { //사용자 이름 유일한지 않으면
            return AjaxResult.error("사용자 추가'" + user.getUserName() + "'실패, 로그인 계정 존재합니다");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) { //사용자 전화번호 유일하지 않으면
            return AjaxResult.error("사용자 추가'" + user.getUserName() + "'실패, 전화번호 존재합니다");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) { //사용자 이메일 유일하지 않으면
            return AjaxResult.error("사용자 추가'" + user.getUserName() + "'실패, 이메일 존재합니다");
        }
        user.setCreateBy(SecurityUtils.getUsername());//사용자 생성자를 로그인 사용자로 설정
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));//사용자 비밀번호를 SecurityUtils()로 암호화하고 비밀번호를 재설정
        return toAjax(userService.insertUser(user));//사용자를 추가하고 응답 메시지를 반환
    }

    /**
     * 사용자 수정
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")//사용자 권한 체크
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);//사용자를 수정할 수 있는지 체크(관리자를 수정할 수 없다)
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) { //전화번호 유일하지 않으면
            return AjaxResult.error("사용자 수정'" + user.getUserName() + "'실패, 전화번호 존재합니다");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) { //이메일 유일하지 않으면
            return AjaxResult.error("사용자 수정'" + user.getUserName() + "'실패, 이메일 존재합니다");
        }
        user.setUpdateBy(SecurityUtils.getUsername());//사용자 업데이트자를 로그인 사용자로 설정
        return toAjax(userService.updateUser(user));//사용자 정보를 수정하고 응답 메시지를 반납
    }

    /**
     * 사용자 삭제
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")//사용자 권한 체크
    @DeleteMapping("/{userIds}")//메소드는 delete 동적 요청을 처리
    public AjaxResult remove(@PathVariable Long[] userIds) {
        return toAjax(userService.deleteUserByIds(userIds));//지정한 사용자 목록을 삭제하고 응답 메시지를 반납
    }

    /**
     * 비밀번호 재설정
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")//사용자 권한 체크
    @PutMapping("/resetPwd")//메소드는 put 동적 요청을 처리
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);//사용자를 조작할 수 있는지 체크(관리자를 조작할 수 없다)
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));//사용자 비밀번호를 SecurityUtils() 로 암호화하고 비밀번호를 재설정
        user.setUpdateBy(SecurityUtils.getUsername());//업데이트자를 로그인 사용자로 설정
        return toAjax(userService.resetPwd(user));//비밀번호를 재설정하고 응답 메세지를 반납
    }

    /**
     * 상태 수정
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")//사용자 권한 체크
    @PutMapping("/changeStatus")//메소드는 put 동적 요청을 처리
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);//사용자를 조작할 수 있는지 체크(관리자를 조작할 수 없다)
        user.setUpdateBy(SecurityUtils.getUsername());//업데이트자를 로그인 사용자로 설정
        return toAjax(userService.updateUserStatus(user));//상태를 설정하고 응답 메세지를 반납
    }
}



