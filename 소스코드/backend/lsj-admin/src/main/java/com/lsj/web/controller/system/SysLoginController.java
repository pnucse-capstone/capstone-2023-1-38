package com.lsj.web.controller.system;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.lsj.common.constant.Constants;
import com.lsj.common.core.domain.AjaxResult;
import com.lsj.common.core.domain.entity.SysMenu;
import com.lsj.common.core.domain.entity.SysUser;
import com.lsj.common.core.domain.model.LoginBody;
import com.lsj.common.core.domain.model.LoginUser;
import com.lsj.common.utils.ServletUtils;
import com.lsj.framework.web.service.SysLoginService;
import com.lsj.framework.web.service.SysPermissionService;
import com.lsj.framework.web.service.TokenService;
import com.lsj.system.service.ISysMenuService;

/**
 * 로그인 체크
 */
@RestController //컨트롤러의 모든 메서드는 응답 반납 表示这个控制器的所有方法都会返回响应体
public class SysLoginController {
    @Autowired  //컨트롤러가 SysLoginService에서 제공하는 메서드를 사용하도록 허용
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * 로그인 메소드
     *
     * @param loginBody 로그인 정보
     * @return 결과
     */
    @PostMapping("/login")  //메소드는 post 요청 경로를 처리
    public AjaxResult login(@RequestBody LoginBody loginBody) { //@RequestBody: 요청체에서 로그인 데이터를 loginBody에 매핑
        AjaxResult ajax = AjaxResult.success();//성공을 표시하는 AjaxResult 객체를 생성 {"code":200,"msg":"액션 성공"}
        //login() 호출하여 로그인 인증 처리하고 token 생성하기
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);//token을 키값쌍으로 ajax 객체에 저장
        return ajax;//응답 결과로 ajax 객체를 반납
    }

    /**
     * 사용자 정보 얻기
     *
     * @return 사용자 정보
     */
    @GetMapping("getInfo")//메소드는 get 요청 경로를 처리
    public AjaxResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());//request에서 사용자 로그인 정보를 얻기
        SysUser user = loginUser.getUser();//로그인 사용자 객체를 얻기
        Set<String> roles = permissionService.getRolePermission(user);// 역할 권한 집합 얻기
        Set<String> permissions = permissionService.getMenuPermission(user);// 메뉴 권한 집합 얻기
        AjaxResult ajax = AjaxResult.success();//ajax객체 생성, 응답 데이터를 저장
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 라우팅 정보 얻기
     *
     * @return 라우팅
     */
    @GetMapping("getRouters")//메소드는 get 요청 경로를 처리
    public AjaxResult getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());//request에서 사용자 로그인 정보를 얻기
        SysUser user = loginUser.getUser();//로그인 사용자 객체를 얻기
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());//사용자 ID로 메뉴 목록 메뉴을 얻기
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}








