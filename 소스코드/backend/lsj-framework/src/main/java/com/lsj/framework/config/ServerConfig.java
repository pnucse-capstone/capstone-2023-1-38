package com.lsj.framework.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import com.lsj.common.utils.ServletUtils;


@Component
public class ServerConfig {
    /**
     * 도메인 이름, 포트, 컨텍스트 액세스 경로를 포함한 전체 요청 경로 얻기
     *
     * @return
     */
    public String getUrl() {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();

        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();


    }
}
