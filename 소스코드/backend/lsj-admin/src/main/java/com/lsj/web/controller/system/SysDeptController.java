package com.lsj.web.controller.system;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
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
import com.lsj.common.core.domain.entity.SysDept;
import com.lsj.common.utils.SecurityUtils;
import com.lsj.common.utils.StringUtils;
import com.lsj.system.service.ISysDeptService;

/**
 * 부서 정보
 */
@RestController //HTTP 요청을 처리하며 데이터 응답을 반납
@RequestMapping("/system/dept") //HTTP 요청은 Controller 클래스에 mapping하고 Controller에서 처리하는 URL prefix를 지정함
public class SysDeptController extends BaseController {
    @Autowired //인터페이스 객체 주입
    private ISysDeptService deptService;//부서 service 인터페이스

    /**
     * 부서 list을 얻기
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")//사용자 권한 체크
    @GetMapping("/list")//메소드는 "/list"경로의 get 요청을 처리
    public AjaxResult list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);//부서 목록 조회
        return AjaxResult.success(depts);
    }

    /**
     * 부서 목록 조회 (노드 제외)
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")//사용자 권한 체크
    @GetMapping("/list/exclude/{deptId}")//메소드는 "/list/exclude/{deptId}"경로의 get 요청을 처리
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        //value:경로 변수의 이름을 지정
        //required=false: 해당 경로 변숟가 필수 아님
        List<SysDept> depts = deptService.selectDeptList(new SysDept());//모든 부서를 조회
        Iterator<SysDept> it = depts.iterator();//iterator 생성
        while (it.hasNext()) {
            SysDept d = (SysDept) it.next();//다음 요소를 얻어서 (SysDept)로 형변환

            //SysDept의 deptId와 주어진 deptId 값이 같거나 d의 상위 목록에 deptId 있으면 SysDept 객체를 제거
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                //StringUtils.split(d.getAncestors(), ","):d객체의ancestors값을 콤마로 분리한 배열을 얻기
                //ArrayUtils.contains: 해당 배열에 deptID 값이 포함되어 있는지 확인
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * 부서 번호로 정보를 얻기
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")//사용자 권한 체크
    @GetMapping(value = "/{deptId}")//메소드는 "deptId"경로의 get 요청을 처리
    public AjaxResult getInfo(@PathVariable Long deptId) {
        return AjaxResult.success(deptService.selectDeptById(deptId));//해당 ID로 부서 정보를 얻기
    }

    /**
     * 부서 드롭다운 트리 모록을 얻기
     */
    @GetMapping("/treeselect")//메소드는 "treeselect"경로의 get 요청을 처리
    public AjaxResult treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);//부서 목록 조회
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));//부서 트리를 구성
    }

    /**
     * 해당 역할 부서 목록 트리 로그
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}") //"/roleDeptTreeselect/{roleId}" 경로에 대한 GET 요청을 처리
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());//모든 부서를 조회
        AjaxResult ajax = AjaxResult.success();//성공 응답
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * 부서 추가
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")//사용자 권한 체크
    @PostMapping //메소드는 POST 요청 처리
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        //@Validated: 메소드 parameter 대해 체크
        //@RequestBody: request에 있는 JSON 데이터를 dept 객체에 mapping한다

        //부서 이름 유일하지 않으면
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) { //부서 이름 유일하는지 체크
            //오류 메시지 반환
            return AjaxResult.error("부서 추가'" + dept.getDeptName() + "'실패, 부서 이름 존재");
        }
        //부서 이름 유일
        dept.setCreateBy(SecurityUtils.getUsername());//부서 생성자를 로그인 사용자로 설정
        return toAjax(deptService.insertDept(dept));//부서 정보를 삽입하고 결과를 반환
    }

    /**
     * 부서 수정
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")//system:dept:edit 권한이 있는 사용자만 액세스할 수 있다
    @PutMapping//메소드는 PUT 요청을 처리
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        //@Validated: 메소드 parameter 대해 체크
        //@RequestBody: request에 있는 JSON 데이터를 dept 객체에 mapping한다

        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) { //부서 이름 유일하지 않으면
            return AjaxResult.error("부서 수정'" + dept.getDeptName() + "'실패, 부서 이름 존재");
        } else if (dept.getParentId().equals(dept.getDeptId())) { //지금의 부서 ID와 항위 부서 ID 같으면
            return AjaxResult.error("부서 수정'" + dept.getDeptName() + "'실패, 상위 부서는 자신이 될 수 없습니다");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0) {//부서 상태 정지이면서 하위 부서이으면
            return AjaxResult.error("사용하고 있는 자식 부서가 있습니다");
        }
        //부서 이름 유일
        dept.setUpdateBy(SecurityUtils.getUsername());//부서 객체의 업데이트자가 현재 로그인한 사용자로 기록
        return toAjax(deptService.updateDept(dept));//부서 정보를 업데이트하고 결과를 return
    }

    /**
     * 부서 삭제
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")//system:dept:remove 권한이 있는 사용자만 액세스할 수 있다
    @DeleteMapping("/{deptId}")//메소드는 "deptId"경로의 delete 요청을 처리
    public AjaxResult remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) { //하위 부서가 있으면
            return AjaxResult.error("하위 부서가 존재하며 삭제할 수 없습니다");
        }
        if (deptService.checkDeptExistUser(deptId)) { //부서에 해당 사용자 존재하면
            return AjaxResult.error("부서에 사용자가 있어 삭제할 수 없습니다");
        }
        return toAjax(deptService.deleteDeptById(deptId));//지정한 부서를 삭제하며 메시지 반납
    }
}
