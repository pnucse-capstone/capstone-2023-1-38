package com.lsj.common.core.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.lsj.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 메뉴 권한 테이블 sys_menu
 */
public class SysMenu extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 메뉴 ID
     */
    private Long menuId;

    /**
     * 메뉴 이름
     */
    private String menuName;

    /**
     * 상위 메뉴 이름
     */
    private String parentName;

    /**
     * 상위 메뉴 ID
     */
    private Long parentId;

    /**
     * 표시 순서
     */
    private String orderNum;

    /**
     * router 주소
     */
    private String path;

    /**
     * 그룹 경로
     */
    private String component;


    /**
     * 캐시하는지 여부(0캐시, 1캐시안 함)
     */
    private String isCache;

    /**
     * 유형(M목록 C메뉴 F버튼)
     */
    private String menuType;

    /**
     * 상태 표시(0표시 1숨기기)
     */
    private String visible;

    /**
     * 메뉴 상태(0표시 1숨기기)
     */
    private String status;

    /**
     * 권한 문자열
     */
    private String perms;

    /**
     * 메뉴 아니콘
     */
    private String icon;

    /**
     * 하위 메뉴
     */
    private List<SysMenu> children = new ArrayList<SysMenu>();

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @NotBlank(message = "메뉴 이름은 비워둘 수 없습니다.")
    @Size(min = 0, max = 50, message = "메뉴 이름 길이는 50자를 초과할 수 없습니다.")
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @NotBlank(message = "표시 순서는 비워둘 수 없습니다.")
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    @Size(min = 0, max = 200, message = "경로 주소는 200자를 초과할 수 없습니다.")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Size(min = 0, max = 200, message = "컴포넌트 경로는 255자를 초과할 수 없습니다.")
    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getIsCache() {
        return isCache;
    }

    public void setIsCache(String isCache) {
        this.isCache = isCache;
    }

    @NotBlank(message = "메뉴 유형은 비워둘 수 없습니다.")
    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Size(min = 0, max = 100, message = "권한 표시 길이는 100자를 초과할 수 없습니다.")
    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<SysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("menuId", getMenuId())
                .append("menuName", getMenuName())
                .append("parentId", getParentId())
                .append("orderNum", getOrderNum())
                .append("path", getPath())
                .append("component", getComponent())
                .append("IsCache", getIsCache())
                .append("menuType", getMenuType())
                .append("visible", getVisible())
                .append("status ", getStatus())
                .append("perms", getPerms())
                .append("icon", getIcon())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
