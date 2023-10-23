package com.lsj.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.lsj.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lsj.common.constant.UserConstants;
import com.lsj.common.core.domain.TreeSelect;
import com.lsj.common.core.domain.entity.SysMenu;
import com.lsj.common.core.domain.entity.SysRole;
import com.lsj.common.core.domain.entity.SysUser;
import com.lsj.common.utils.SecurityUtils;
import com.lsj.common.utils.StringUtils;
import com.lsj.system.domain.vo.MetaVo;
import com.lsj.system.domain.vo.RouterVo;
import com.lsj.system.mapper.SysMenuMapper;
import com.lsj.system.mapper.SysRoleMapper;
import com.lsj.system.mapper.SysRoleMenuMapper;

/**
 * 메뉴 업무 층 처리
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {
//    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    /**
     * 사용자 따라 시스템 메뉴 목록 조회
     *
     * @param userId 사용자 ID
     * @return 메뉴 목록
     */
    @Override
    public List<SysMenu> selectMenuList(Long userId) {
        return selectMenuList(new SysMenu(), userId);
    }

    /**
     * 사용자 따라 시스템 메뉴 목록 조회
     *
     * @param menu   메뉴 정보
     * @param userId 사용자 ID
     * @return 메뉴 목록
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId) {
        List<SysMenu> menuList = null;//조회 결과를 저장
        //관리자는 모든 메뉴 정보를 보여준다
        if (SysUser.isAdmin(userId)) { //사용자가 관리자인지 확인
            menuList = menuMapper.selectMenuList(menu);//전체 메뉴 정보를 조회
        } else { //관리자 아닌 경우
            menu.getParams().put("userId", userId);//params 속성에 사용자 ID의 키값쌍을 추가
            menuList = menuMapper.selectMenuListByUserId(menu);//사용자 접근할 수 있는 메뉴 정보 조회
        }
        return menuList;
    }

    /**
     * 사용자 ID로 메뉴 권한 조회
     *
     * @param userId 사용자 ID
     * @return 권한 목록
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);//useId로 메뉴 권한 목록을 조회
        Set<String> permsSet = new HashSet<>();//HashSet 집합 생성, 메뉴 권한 저장
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));//현재 메뉴 권한 문자열을 콤마로 구분한 후 permsSet에 추가
            }
        }
        return permsSet;
    }

    /**
     * 사용자 ID로 메뉴트리 정보 조회(目录和菜单)
     *
     * @param userId 사용자 ID
     * @return 메뉴 목록
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = null;
        if (SecurityUtils.isAdmin(userId)) { //관지자 인지 판단
            menus = menuMapper.selectMenuTreeAll();//모든 메뉴트리목록을 조회

        } else {
            menus = menuMapper.selectMenuTreeByUserId(userId);//사용자 Id로 메뉴 목록을 조회
        }
        return getChildPerms(menus, 0);//상위 노드 ID로 하위 노드 얻어서 반납
    }

    /**
     * 역할 ID로 메뉴트리 정보 조회
     *
     * @param roleId 사용자 ID
     * @return 메뉴 목록 选中菜单列表
     */
    @Override
    public List<Integer> selectMenuListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);//역할 ID로 역할 객체를 얻기
        return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());//메뉴 목록을 얻기
    }

    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();//라우팅 구성 객체 생성
            router.setHidden("1".equals(menu.getVisible()));//메뉴 상태 따라 경로 표시 여부 설정
            router.setName(getRouteName(menu));//메뉴 이름을 얻어서 라우팅 이름을 설정
            router.setPath(getRouterPath(menu));//메뉴 라우팅 경로를 얻어서 라우팅 주소를 설정
            router.setComponent(getComponent(menu));//컴포넌트 주소를 설정
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache())));//라우팅 정보 설정
            List<SysMenu> cMenus = menu.getChildren();//현재 메뉴의 하위 메뉴 목록 얻기
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {//하위 메뉴 있으며 유형은 목록이면
                router.setAlwaysShow(true);//항상 표시로 설정
                router.setRedirect("noRedirect");//라우팅 리디렉션 noRedirect로 설정(设置无法点击跳转)
                router.setChildren(buildMenus(cMenus));//buildMenus 메서드를 재호출하여 현재 메뉴의 하위 메뉴 라우팅 구성을 설정
            } else if (isMeunFrame(menu)) {//메뉴가 부모 목록 메뉴이면
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());//하위 라우팅 주소를 설정
                children.setComponent(menu.getComponent());//하위 컴포넌트 주소 설정
                children.setName(StringUtils.capitalize(menu.getPath()));//하위 라우팅 이름 설정 capitalize:경로의 첫 글자를 대문자로
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache())));//라우팅 정보 설정
                childrenList.add(children);
                router.setChildren(childrenList);//childrenList로 현재 메뉴의 하위 메뉴로 설정
            }
            routers.add(router);//라우팅 목록에서 라우팅 구성을 추가
        }
        return routers;
    }

    /**
     * 프런트 엔드에 필요한 트리 구조 구축
     *
     * @param menus 메뉴 목록
     * @return 트리 구조 목록
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();//하위 노드 갗추는 상위 노드를 저장
        List<Long> tempList = new ArrayList<Long>();//메뉴 목록의 모든 자식 메뉴의 menuId를 저장
        for (SysMenu dept : menus) {
            tempList.add(dept.getMenuId());//각 메뉴의 ID를 tempList에 저장
        }
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            //iterator(): menus의 iterator 생성
            //hasNext(): next element있는지 확인
            SysMenu menu = (SysMenu) iterator.next();//iterator에 다음 element 반납
            // 최상위 노드인 경우 상위 노드의 모든 하위 노드를 방문 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) { //현재 menu 객체의 상위 노드가 tempList에 포함되어 있는지 확인(없으면 최종 노드)
                /*상위 노드인 경우 상위 노드의 모든 자식 노드를 recursion하고 재귀 함수 recursionFn을 호출하여
                매개 변수 메뉴와 현재 메뉴 개체 menu를 전달하여 메뉴 개체의 자식 노드를 처리한다.*/
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {//상위 노드 없으면 menus로 반납
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 프런트 엔드에 필요한 드롭다운 트리 구조 구축
     * @param menus 메뉴 목록
     * @return 드롭다운 트리 구조 목록
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);//하위 노드 갗추는 상위 노드 list
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 메뉴 ID로 메뉴 정보를 조회
     * @param menuId 메뉴 ID
     * @return 메뉴 정보
     */
    @Override
    public SysMenu selectMenuById(Long menuId) {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * 메뉴 자식 노드가 있는지 여부
     *
     * @param menuId 메뉴 ID
     * @return 결과 true 존재 false 존재하지 않음
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result > 0 ? true : false;
    }

    /**
     * 메뉴에 해당 역할이 있는지 조회
     *
     * @param menuId 메뉴 ID
     * @return 결과 true 존재 false 존재하지 않음
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        int result = roleMenuMapper.checkMenuExistRole(menuId);
        return result > 0 ? true : false;
    }

    /**
     * 메뉴 정보 추가
     *
     * @param menu 메뉴 정보
     * @return 결과
     */
    @Override
    public int insertMenu(SysMenu menu) {
        return menuMapper.insertMenu(menu);
    }

    /**
     * 메뉴 정보 수정
     *
     * @param menu 메뉴 정보
     * @return 결과
     */
    @Override
    public int updateMenu(SysMenu menu) {
        return menuMapper.updateMenu(menu);
    }

    /**
     * 메뉴 관리 정보 삭제
     *
     * @param menuId 메뉴 ID
     * @return 결과
     */
    @Override
    public int deleteMenuById(Long menuId) {
        return menuMapper.deleteMenuById(menuId);
    }

    /**
     * 메뉴 이름은 유일한지 체크
     *
     * @param menu 메뉴 정보
     * @return 결과
     */
    @Override
    public String checkMenuNameUnique(SysMenu menu) {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();//메뉴ID를 얻기, null이면 -1L
        //지정한 메뉴 이름과 같은 상위 노드 정보를 조회(없으면 null)
        SysMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        //조회 결과 null아니고 메뉴ID가 현재 메뉴ID 다르면 해당 메뉴 이름은 존재함
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
            return UserConstants.NOT_UNIQUE;//유일하지 않으로 반납
        }
        return UserConstants.UNIQUE;//유일함으로 반납
    }

    /**
     * 라우팅 이름을 얻기
     *
     * @param menu 메뉴 정보
     * @return 라우팅 이름
     */
    public String getRouteName(SysMenu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());//결로 이름 얻기  capitalize:경로의 첫 글자를 대문자로
        //메뉴의 상위 노드 0이고 메뉴 유형은 메뉴이면
        if (isMeunFrame(menu)) {
            routerName = StringUtils.EMPTY;//라우팅 이름을 빈 문자열로 설정
        }
        return routerName;
    }

    /**
     * 라우팅 주소를 얻기
     *
     * @param menu 메뉴 정보
     * @return 라우팅 주소
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        //메뉴의 상위 노드 0이고 메뉴 유형은 목록이면
        if ( menu.getParentId().intValue() == 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())
        ) {  //&& UserConstants.NO_FRAME.equals(menu.getIsFrame())
            routerPath = "/" + menu.getPath();//경로에 "/"를 붙여준다
        }
        //메뉴의 상위 노드 0이고 메뉴 유형은 목록이면
        else if (isMeunFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 컴포넌트 정보를 얻기
     *
     * @param menu 메뉴 정보
     * @return 컴포넌트 정보
     */
    public String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;//컴포넌트 주소를 초기화
        //메뉴 컴포넌트 null 아니고 상위 노드 아니면
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMeunFrame(menu)) {
            component = menu.getComponent();//라우팅의 컴포넌트 주소는 menu의 component로 설정
        }
        return component;
    }

    /**
     * 메뉴가 부모 노드 메뉴인지 여부
     * @param menu 메뉴 정보
     * @return 결과
     */
    public boolean isMeunFrame(SysMenu menu) {
        //메뉴의 상위 노드 0이고 메뉴 유형은 메뉴이다
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType());
    }

    /**
     * 상위 노드의 ID로 모든 하위 노드를 얻기
     * @param list     분류표
     * @param parentId 항위 노드 ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();//하위 노드 목록 생성
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();//다음 원소를 방문
            //전달된 부모 노드ID에 따라 부모 노드의 모든 자식 노드를 순회
            if (t.getParentId() == parentId) { //메뉴 객체t의 상위 노드 Id와 parentId 같으면 t가 parentId의 자식노드
                recursionFn(list, t);//재귀적으로 모든 하위 메뉴 찾기
                returnList.add(t);//찾은 메뉴 객체를 returnList에 저장
            }
        }
        return returnList;//저장 된 자식 메뉴 목록 반납
    }

    /**
     * recursion list
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        List<SysMenu> childList = getChildList(list, t);//자식 노드 목록을 얻기
        t.setChildren(childList);//자식 노드 childList를 메뉴 객체 t의 자식 노드로 설정
        for (SysMenu tChild : childList) {//childList의 각 요소를 방문
            if (hasChild(list, tChild)) {//자식 노드 tChild가 자식 노드 더 있는 지 확인
                recursionFn(list, tChild);//있으면 recursionFn메소드 재 호출
            }
        }
    }

    /**
     * 지정한 메뉴 객체의 하위 노드 목록을 얻기
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();//자식 노드를 저장하는 list 생성
        Iterator<SysMenu> it = list.iterator();//메뉴 list의 iterator 생성
        while (it.hasNext()) { //각 메뉴 객체를 방문
            SysMenu n = (SysMenu) it.next();//현재 방문된 메뉴 객체를 얻기

            //현재 메뉴 n의 상위 메뉴 ID가 주어진 t메뉴 ID 같은지 확인
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);//n를 자식 노드 모록 tlist에 저장
            }
        }
        return tlist;//자식 노드 목록을 반납
    }

    /**
     * 자식 노드가 있는지 확인
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
