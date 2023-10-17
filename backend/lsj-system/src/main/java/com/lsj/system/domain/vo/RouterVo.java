package com.lsj.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * 라우팅 구성 정보
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)//JSON 직렬화를 수행할 때, 빈 값이나 null 값인 속성들을 JSON에 포함시키지 않도록 지정
public class RouterVo {
    /**
     * 라우팅 이름
     */
    private String name;

    /**
     * 라우팅 주소
     */
    private String path;

    /**
     * 경로 숨기기 여부, true로 설정하면 사이드바에 경로가 표시되지 않다 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    private boolean hidden;

    /**
     * 리디렉션 주소, noRedirect가 설정된 경우 이동 경로 탐색에서 경로를 클릭할 수 없다 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    private String redirect;

    /**
     * 컴포넌트 주소 组件地址
     */
    private String component;

    /**
     * 경로 아래의 자식이 선언한 경로가 1보다 크면 자동으로 구성 요소 페이지와 같은 중첩 모드가 된다 当一个路由下面的children声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     */
    private Boolean alwaysShow;

    /**
     * 기타 요소
     */
    private MetaVo meta;

    /**
     * 하위 라우팅
     */
    private List<RouterVo> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Boolean getAlwaysShow() {
        return alwaysShow;
    }

    public void setAlwaysShow(Boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    public MetaVo getMeta() {
        return meta;
    }

    public void setMeta(MetaVo meta) {
        this.meta = meta;
    }

    public List<RouterVo> getChildren() {
        return children;
    }

    public void setChildren(List<RouterVo> children) {
        this.children = children;
    }
}
