package com.lsj.common.core.domain;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lsj.common.core.domain.entity.SysDept;
import com.lsj.common.core.domain.entity.SysMenu;

/**
 * Treeselect 트리 구조 엔터티 클래스
 */
public class TreeSelect implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 노드 ID */
    private Long id;

    /** 노드 이름 */
    private String label;

    /** 자식 노드 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)//속성 값이 null이거나 빈 문자열인 경우 속성이 생성된 JSON 문자열에 포함되지 않게 된다
    private List<TreeSelect> children;

    public TreeSelect() {}

    public TreeSelect(SysDept dept) {
        this.id = dept.getDeptId();
        this.label = dept.getDeptName();
        this.children = dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public TreeSelect(SysMenu menu) {
        this.id = menu.getMenuId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeSelect> getChildren() {
        return children;
    }

    public void setChildren(List<TreeSelect> children) {
        this.children = children;
    }
}
