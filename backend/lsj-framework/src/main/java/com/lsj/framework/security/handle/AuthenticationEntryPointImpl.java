package com.lsj.framework.security.handle;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.lsj.common.constant.HttpStatus;
import com.lsj.common.core.domain.AjaxResult;
import com.lsj.common.utils.ServletUtils;
import com.lsj.common.utils.StringUtils;

/**
 * 인증 실패 처리 클래스
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) { //이증 실패 처리
        int code = HttpStatus.UNAUTHORIZED;//상태 코드를 401(허가되지 않은)로 설정
        //요청 경로를 얻어서 동적 오류 메시지를 만들기
        String msg = StringUtils.format("요청된 액세스: {}, 인증 실패, 시스템 리소스에 액세스할 수 없습니다.", request.getRequestURI());
        //오류 메시지를 JSON 형식으로 변환하여 HTTP 응답 객체로 클라이언트에 반납
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
    }
}
