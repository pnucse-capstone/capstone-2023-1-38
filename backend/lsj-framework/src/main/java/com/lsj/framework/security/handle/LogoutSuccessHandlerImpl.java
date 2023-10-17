package com.lsj.framework.security.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lsj.framework.manager.AsyncManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import com.alibaba.fastjson.JSON;
import com.lsj.common.constant.Constants;
import com.lsj.common.constant.HttpStatus;
import com.lsj.common.core.domain.AjaxResult;
import com.lsj.common.core.domain.model.LoginUser;
import com.lsj.common.utils.ServletUtils;
import com.lsj.common.utils.StringUtils;
import com.lsj.framework.manager.factory.AsyncFactory;
import com.lsj.framework.web.service.TokenService;

/**
 * 퇴출 처리 클래스
 */
@Configuration//전역 설정 클래스
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Autowired
    private TokenService tokenService;

    /**
     * 퇴출 처리
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser loginUser = tokenService.getLoginUser(request);//request에서 사용자 로그인 정보를 얻기
        if (StringUtils.isNotNull(loginUser)) { //로그인 정보 null아니면
            String userName = loginUser.getUsername();//로그인 정보중에 사용자 이름 얻기
            //사용자 캐시 기록을 삭제
            tokenService.delLoginUser(loginUser.getToken());
            //사용자 퇴출 로그 기록
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "종료 성공"));
        }
        //성공 상태 코드 및 응답 정보를 JSON 형식으로 변환하고 클라이언트에 반납
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(HttpStatus.SUCCESS, "종료 성공")));
    }
}
