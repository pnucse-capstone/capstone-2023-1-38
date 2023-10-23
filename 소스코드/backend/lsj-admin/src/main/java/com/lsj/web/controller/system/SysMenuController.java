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
import com.lsj.common.core.domain.entity.SysMenu;
import com.lsj.common.core.domain.model.LoginUser;
import com.lsj.common.utils.SecurityUtils;
import com.lsj.common.utils.ServletUtils;
import com.lsj.framework.web.service.TokenService;
import com.lsj.system.service.ISysMenuService;

/**
 * 메뉴 정보
 */
@RestController //HTTP 요청을 처리하며 데이터 응답을 반납
@RequestMapping("/system/menu") //HTTP 요청은 Controller 클래스에 mapping하고 Controller에서 처리하는 URL prefix를 지정함
public class SysMenuController extends BaseController {
    @Autowired //인터페이스 구현 객체 주입
    private ISysMenuService menuService;//메뉴 service 인터페이스

    @Autowired
    private TokenService tokenService;//token service 클래스

    /**
     * 메뉴 목록 얻기
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")//사용자 권한 치크
    @GetMapping("/list")//메소드는 get 요청 처리
    public AjaxResult list(SysMenu menu) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());//request에서 사용자 로그인 정보를 얻기
        Long userId = loginUser.getUser().getUserId();// 로그인 사용자 객체에서 사용자 ID 얻기
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);//menu 객체와 사용자 ID 따라 메뉴 목록을 조회
        return AjaxResult.success(menus);//조회된 menus는 AjaxResult 객체로 캡슐화되어 반환
    }

    /**
     * 메뉴 번호로 메뉴 정보를 얻기
     */
    //@PreAuthorize 액세스 제어 주석
    //@ss.hasPermi 자체 정의 주석: 권한 확인 및 액세스 제어
    @PreAuthorize("@ss.hasPermi('system:menu:query')")//사용자 권한 체크
    @GetMapping(value = "/{menuId}")//get 요청 처리, 동적 요청 경로 설정
    public AjaxResult getInfo(@PathVariable Long menuId) { //@PathVariable: URL 경로에서 변수 값 추출하여 메소드 메개변수에서 사용
        //menuId 따라 메뉴 정보를 얻어서 결과를 AjaxResult 객체에 저장
        return AjaxResult.success(menuService.selectMenuById(menuId));
    }

    /**
     * 메뉴 드롭다운 트리 목록을 얻기
     */
    @GetMapping("/treeselect")//메소드는 get 요청 처리
    public AjaxResult treeselect(SysMenu menu) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());//request에서 사용자 로그인 정보를 얻기
        Long userId = loginUser.getUser().getUserId();//로그인 사용자 객체에서 사용자 ID를 얻기
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);//menu객체과 사용자ID로 메뉴 목록 조회
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));//메뉴 목록으로 드롭다운 트리 구조 목록 얻어서 응답 메시지 반납
    }

    /**
     * 해당 역할 메뉴 목록 트리 로드
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")//메소드는 get 동적 요청 처리
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());//로그인 사용자 정보를 얻기
        List<SysMenu> menus = menuService.selectMenuList(loginUser.getUser().getUserId());//사용자 Id로 메뉴 목록을 얻기
        AjaxResult ajax = AjaxResult.success();//AjaxResult 객체를 생성
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    /**
     * 메뉴 추가
     */
    @PreAuthorize("@ss.hasPermi('system:menu:add')")//system:menu:add 권한이 있는 사용자만 액세스할 수 있다
    @PostMapping//add 메소드는 POST 요청을 처리
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        //@Validated: 메소드 parameter 대해 체크
        //@RequestBody: request에 있는 JSON 데이터를 menu 객체에 mapping한다

        //메뉴 이름 유일하지 않으면
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            //오류 메시지 반납
            return AjaxResult.error("메뉴 추가'" + menu.getMenuName() + "'실패, 메뉴 이름 존재합니다");
        }
        //메뉴 이름 유일하면
        menu.setCreateBy(SecurityUtils.getUsername());//메뉴 생성자를 로그인 사용자로 설정
        return toAjax(menuService.insertMenu(menu));//새 메뉴 정보를 삽입하고 결과를 반납
    }

    /**
     * 메뉴 수정
     */
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")//system:menu:edit 권한이 있는 사용자만 액세스할 수 있다
    @PutMapping//메소드는 PUT 요청을 처리
    public AjaxResult edit(@Validated @RequestBody SysMenu menu) {
        //@Validated: 메소드 parameter 대해 체크
        //@RequestBody: request에 있는 JSON 데이터를 menu 객체에 mapping한다

        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {//메뉴 이름 유일하지 않으면
            return AjaxResult.error("메뉴 수정'" + menu.getMenuName() + "'실패, 메뉴 이름 존재합니다");
        } else if (menu.getMenuId().equals(menu.getParentId())) { //현재 메뉴의 ID와 상위 ID 같으면
            return AjaxResult.error("메뉴 수정'" + menu.getMenuName() + "'실패, 상위 메뉴에서 자신을 선택할 수 없습니다");
        }
        //메뉴 이름 유일
        menu.setUpdateBy(SecurityUtils.getUsername());//메뉴 개체의 업데이트자가 현재 로그인한 사용자로 설정
        return toAjax(menuService.updateMenu(menu));//메뉴 정보를 업데이트하고 결과를 봔환
    }

    /**
     * 메뉴 삭제
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")//system:menu:remove 권한이 있는 사용자만 액세스할 수 있다
    @DeleteMapping("/{menuId}")//메소드는 DELETE {menuId}동적 요청을 처리
    public AjaxResult remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {//하위 메뉴 있는지 확인
            return AjaxResult.error("하위 메뉴가 있으며 삭제할 수 없습니다");
        }
        if (menuService.checkMenuExistRole(menuId)) {//메뉴 Id로 메뉴에 해당 역할이 있는지 체크
            return AjaxResult.error("메뉴가 이미 지정되어 삭제할 수 없습니다");
        }
        return toAjax(menuService.deleteMenuById(menuId));//지정한 메뉴를 삭제하여 결과를 return
    }
}





