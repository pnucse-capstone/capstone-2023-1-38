package com.lsj.web.controller.system;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.lsj.common.config.lsjConfig;
import com.lsj.common.core.controller.BaseController;
import com.lsj.common.core.domain.AjaxResult;
import com.lsj.common.core.domain.entity.SysUser;
import com.lsj.common.core.domain.model.LoginUser;
import com.lsj.common.utils.SecurityUtils;
import com.lsj.common.utils.ServletUtils;
import com.lsj.common.utils.file.FileUploadUtils;
import com.lsj.framework.web.service.TokenService;
import com.lsj.system.service.ISysUserService;

/**
 * 개인 정보, 업무 처리
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * 개인 정보
     */
    @GetMapping//메소드는 get 요청을 처리
    public AjaxResult profile() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());//사용자 로그인 정보를 얻기
        SysUser user = loginUser.getUser();//로그인 정보 통해 사용자 정보를 얻기
        AjaxResult ajax = AjaxResult.success(user);//성공 응답 데이터를 설정
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }

    /**
     * 사용자 수정
     */
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        //@RequestBody: 요청된 JSON 데이터를 SysUser 개체로 변환하는 데 사용됩니다.
        if (userService.updateUserProfile(user) > 0) { //user로 사용자 개인정보를 수정
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());//요청에 가진 token 얻기
            //개시된 사용자 정보 업데이트
            loginUser.getUser().setNickName(user.getNickName());
            loginUser.getUser().setPhonenumber(user.getPhonenumber());
            loginUser.getUser().setEmail(user.getEmail());
            loginUser.getUser().setSex(user.getSex());
            tokenService.setLoginUser(loginUser);//token 업데이트
            return AjaxResult.success();//성공 메시지 반납
        }
        return AjaxResult.error("개인정보 수정 오류가 있으니 관리자에게 문의 바랍니다.");
    }

    /**
     * 비밀 번호 재수정
     */
    @PutMapping("/updatePwd") //메소드는 put 요청을 처리
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());//사용자 로그인 정보를 얻기
        String userName = loginUser.getUsername();//사용자 계정번호 얻기
        String password = loginUser.getPassword();//사용자 원래의 비밀번호 얻기
        if (!SecurityUtils.matchesPassword(oldPassword, password)) { //입력한 원래의 비밀번호 정확하는지 확인
            return AjaxResult.error("비밀번호 변경에 실패했습니다. 이전 비밀번호가 잘못되었습니다.");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) { //입력한 새 비밀번호가 원래 비밀번호와 같은지 확인
            return AjaxResult.error("새 비밀번호는 이전 비밀번호와 같을 수 없습니다.");
        }
        if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword)) > 0) { //사용자 비밀번호 재설정
            //캐시된 사용자 비밀번호 업데이트
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);//캐시된 사용자 정보를 업데이트
            return AjaxResult.success();//성공 응답 메시지 반납
        }
        return AjaxResult.error("비밀 번호 수정 오류가 있으니 관리자에게 문의 바랍니다.");
    }

    /**
     * 프로필사진 업로드
     */
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException { //@RequestParam: 클라이언트가 업로드한 사진 파일 받기
        if (!file.isEmpty()) { //파일 있는지 확인
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());//사용자 로그인 정보를 얻기
            //업로드된 사진 파일을 지정된 경로(lsjConfig.getAvatarPath())에 저장하고, 사진 파일(avatar)의 저장된 경로를 반납
            String avatar = FileUploadUtils.upload(lsjConfig.getAvatarPath(), file);

            //현재 로그인 사용자의 사용자 이름(loginUser.getUsername())과 사진 경로(avatar)로 사용하여 해당 사용자의 사진 정보를 데이터베이스에 업데이트, 성공되면 true를 반납
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                AjaxResult ajax = AjaxResult.success();//성공 메시지 ajax 객체에 저장
                ajax.put("imgUrl", avatar);//사진 경로 저장
                //Cache한 사용자 프로필 사진 업데이트
                loginUser.getUser().setAvatar(avatar);//현재 로그인 사용자의 사진 경로 설정
                tokenService.setLoginUser(loginUser);//업데이트되 로그인 사용자 정보를 캐시하기
                return ajax;//프런트 엔드로 ajax 반납
            }
        }
        return AjaxResult.error("사진 업로드 오류가 있으니 관리자에게 문의하세요");
    }
}



