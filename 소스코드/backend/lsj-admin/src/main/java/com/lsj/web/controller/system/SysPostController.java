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
import com.lsj.common.core.page.TableDataInfo;
import com.lsj.common.utils.SecurityUtils;
import com.lsj.system.domain.SysPost;
import com.lsj.system.service.ISysPostService;

/**
 * 직위 정보 조작
 */
@RestController //HTTP 요청을 처리하며 데이터 응답을 반납
@RequestMapping("/system/post") //HTTP 요청은 Controller 클래스에 mapping하고 Controller에서 처리하는 URL prefix를 지정함
public class SysPostController extends BaseController {
    @Autowired //인터페이스 객체 주입
    private ISysPostService postService; //직위 service 인터페이스

    /**
     * 직위 목록을 얻기
     */
    @PreAuthorize("@ss.hasPermi('system:post:list')")//사용자 권한 체크
    @GetMapping("/list")//메소드는 "list"경로의 get 요청을 처리
    public TableDataInfo list(SysPost post) {
        startPage();
        List<SysPost> list = postService.selectPostList(post);//직위 목록 얻기
        return getDataTable(list);//요청에 대해 페이징 데이터 응답
    }

    /**
     * 직위 코드번호 따라 정보를 얻기
     */
    @PreAuthorize("@ss.hasPermi('system:post:query')")//사용자 권한 체크
    @GetMapping(value = "/{postId}")//메소드는 "postId"동적 경로의 get 요청을 처리
    public AjaxResult getInfo(@PathVariable Long postId) {
        return AjaxResult.success(postService.selectPostById(postId));//파라미터 Id로 직위 정보을 얻기
    }

    /**
     * 직위 추가
     */
    @PreAuthorize("@ss.hasPermi('system:post:add')")//사용자 권한 체크
    @PostMapping//메소드는 POST 요청을 처리
    public AjaxResult add(@Validated @RequestBody SysPost post) {
        //@Validated: 메소드 parameter 대해 체크
        //@RequestBody: request에 있는 JSON 데이터를 post 객체에 mapping한다

        //직위 이름 유일하지 않으면
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return AjaxResult.error("직위 추가'" + post.getPostName() + "'실패, 직위 이름 존재합니다");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) { //직위 코드번호 유일하지 않으면
            return AjaxResult.error("직위 추가'" + post.getPostName() + "'실패, 직위 코드번호 존재합니다");
        }
        //직위 이름 유일
        post.setCreateBy(SecurityUtils.getUsername());//직위 생성자를 로그인 사용자로 설정
        return toAjax(postService.insertPost(post));//직위 정보를 삽입하고 결과를 반환
    }

    /**
     * 직위 수정
     */
    @PreAuthorize("@ss.hasPermi('system:post:edit')")//사용자 권한 체크
    @PutMapping//메소드는 PUT 요청을 처리
    public AjaxResult edit(@Validated @RequestBody SysPost post) {
        //@Validated: 메소드 파라미터대해 체크
        //@RequestBody: 요청에 있는 JSON 데이터를 post 객체에 매핑한다

        //직위 이름 유일하지 않으면
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return AjaxResult.error("직위 수정'" + post.getPostName() + "'실패, 직위 이름 존재합니다");// post.getPostName()
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) { //직위 코드번호 유일하지 않으면
            return AjaxResult.error("직위 수정'" + post.getPostName() + "'실패, 직위 코드번호 존재합니다");// post.getPostName()
        }
        post.setUpdateBy(SecurityUtils.getUsername());//직위 객체의 업데이트자가 현재 로그인한 사용자로 설정
        return toAjax(postService.updatePost(post));//직위 정보를 업데이트하고 결과를 return
    }

    /**
     * 직위 삭제
     */
    @PreAuthorize("@ss.hasPermi('system:post:remove')")//사용자 권한 체크
    @DeleteMapping("/{postIds}")//메소드는 "postIds"동적 경로의 delete 요청을 처리
    public AjaxResult remove(@PathVariable Long[] postIds) {
        return toAjax(postService.deletePostByIds(postIds));//ID로 직위 삭제
    }

    /**
     * 직위 옵션선택 목록을 얻기
     */
    @GetMapping("/optionselect")//메소드는 "optionselect"경로의 get 요청을 처리
    public AjaxResult optionselect() {
        List<SysPost> posts = postService.selectPostAll();//모든 직위 조회
        return AjaxResult.success(posts);//모든 직위와 응답메시지 반납
    }
}



