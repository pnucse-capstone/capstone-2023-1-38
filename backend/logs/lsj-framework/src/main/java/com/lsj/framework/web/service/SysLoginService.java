package com.lsj.framework.web.service;

import javax.annotation.Resource;

import com.lsj.framework.manager.AsyncManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.lsj.common.constant.Constants;
import com.lsj.common.core.domain.model.LoginUser;
import com.lsj.common.core.redis.RedisCache;
import com.lsj.common.exception.CustomException;
import com.lsj.common.exception.user.CaptchaException;
import com.lsj.common.exception.user.CaptchaExpireException;
import com.lsj.common.exception.user.UserPasswordNotMatchException;
import com.lsj.common.utils.MessageUtils;
import com.lsj.framework.manager.factory.AsyncFactory;

/**
 * 로그인 체크
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource //필드 또는 메서드에 대해 의존성을 자동으로 주입
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    /**
     * 로그인 체크: 사용자를 신분 인증 및 토큰 생성
     *
     * @param username 사용자 이름
     * @param password 비밀번호
     * @param code     인증코드(입력한 답)
     * @param uuid     유일 식별자
     * @return 결과
     */
    public String login(String username, String password, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;//상수 키와 uuid 연결해서 redis에 저장하는 인증코드의 키를 생성
        String captcha = redisCache.getCacheObject(verifyKey);//Redis 캐시에서 verifyKey에 해당하는 인증코드값을 얻기
        redisCache.deleteObject(verifyKey);//인증 코드를 받은 후 Redis 캐시에서 인증코드를 삭제, 인증 코드를 한 번만 사용할 수 있도록 한다

        if (captcha == null) {  //인증코드 개시가 null이면 인증 코드 만료되었다
            //비동기 실행 작업:recordLogininfor 로그인 정보 기록
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            //AsyncManager.me().execute() 메서드는 비동기 작업을 실행하고 로그인 정보를 기록하고, recordLogininfor() 메서드는 TimerTask 작업을 반납
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {  //사용자가 입력한 인증번호가 획득한 인증번호 captcha(대소문자 무시)와 일치하지 않으면 인증코드가 잘못된 것이다
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }

        /**
         * 사용자 인증
         * */
        //Authentication 인터페이스는 인증과 관련된 메소드와 속성을 정의, 인증 결과를 나타날때 사용
        Authentication authentication = null;
        try {
            /*authenticationManager.authenticate()는 security 내부 API로 웹 로그인 시 내부적으로 authenticationManager.authenticate()를 호출하여
            계정ID과 비밀번호를 확인하고 마지막으로 security는 UserDetailsServiceImpl.loadUserByUsername() 메소드를 호출하여 사용자 인증한다.*/
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) { //비밀번호 오류 Thrown if an authentication request is rejected because the credentials are invalid.
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();//비밀번호 오류 예외
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());//정의 예외
            }
        }
        //로그인 정보 로그 기록(로그인 성공 또는 실패)
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();//인증 된 사용자 정보(Principal:사용자 정보+권한)
        //token 생성
        return tokenService.createToken(loginUser);
    }
}
