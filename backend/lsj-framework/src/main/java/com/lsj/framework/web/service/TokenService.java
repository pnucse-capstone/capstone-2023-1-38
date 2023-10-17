package com.lsj.framework.web.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.lsj.common.constant.Constants;
import com.lsj.common.core.domain.model.LoginUser;
import com.lsj.common.core.redis.RedisCache;
//import com.lsj.common.utils.ServletUtils;
import com.lsj.common.utils.StringUtils;
//import com.lsj.common.utils.ip.AddressUtils;
//import com.lsj.common.utils.ip.IpUtils;
import com.lsj.common.utils.uuid.IdUtils;
//import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token 인증 처리
 * 사용자 신분정보의 token 관리
 */
@Component
public class TokenService {
    /**
     * token 사용자 정의 표시
     */
    @Value("${token.header}")
    private String header;

    /**
     * token 키
     */
    @Value("${token.secret}")
    private String secret;

    /**
     * token 유효 기간 (기본 30분)
     */
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;//1초

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;//10분

    @Autowired
    private RedisCache redisCache;

    /**
     * 사용자 신분 정보를 얻기
     *
     * @return 사용자 정보
     */
    public LoginUser getLoginUser(HttpServletRequest request) { //요청는 메소드 정보를 얻기 参
        //요청에 가진 token를 얻기
        String token = getToken(request);//token 얻기
        if (StringUtils.isNotEmpty(token)) {//token 있는지 확인
            Claims claims = parseToken(token);//token 진짜인지 확인
            // 해당 권한과 사용자 정보 분석
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);//분석된 Claims 개체에서 사용자 고유 ID uuid 가져오기
            String userKey = getTokenKey(uuid);//uuid로 사용자 redis에 있는 userKey를 구성
            LoginUser user = redisCache.getCacheObject(userKey);//userKey로 redis 캐시중에 사용자 정보를 얻기
            return user;//사용자 정보 반납
        }
        return null;
    }

    /**
     * 사용자 신분 정보를 설정
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {//loginUser 있으며 token null 아니면
            refreshToken(loginUser);//새로고침 token
        }
    }

    /**
     * 사용자 신분 정보를 삭제
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);//token 캐시키를 얻기
            redisCache.deleteObject(userKey);//redis에 해당 캐시 데이터를 삭제
        }
    }

    /**
     * token 생성
     *
     * @param loginUser 사용자 정보
     * @return token
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();//사용자 액세스 token로 사용할 uuid를 생성(token的id)
        loginUser.setToken(token);//token 설정(uuid)
        refreshToken(loginUser);//token 유효기간 새로고치고 사용자 정보 redis에 저장

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);//token를 LOGIN_USER_KEY에 해당하는 값으로 claims에 저장()
        return createToken(claims);//token 생성
    }

    /**
     * token 유효기간을 체크
     *
     * @param loginUser
     * @return
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();//만료 시간 얻기
        long currentTime = System.currentTimeMillis();//현재 시간 얻기
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {//유효 기간 지나지 않으면
            refreshToken(loginUser);//token 유효기간 새로거침
        }
    }

    /**
     * 새로고침 token 유효기간
     *
     * @param loginUser 로그인 정보
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());//로그인 시간을 현재 시간로 설정
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);//유효 시간 설정(현재시간+유효시간)
        // uuid따라 loginUser를 redis에 저장
        String userKey = getTokenKey(loginUser.getToken());//key+token 사용자 다시 로그인하면 이 커로 식별
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);//로그인 성공하는 사용자 정보를 redis에 저장(사용자 정보+권한)
    }


    private String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return token;
    }

    /**
     * 토큰에서 데이터 클레임을 얻기 从令牌中获取数据声明
     * @param token 토큰
     * @return 데이터 클레임
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * token중에 사용자 이름 얻기
     * @param token 토큰
     * @return 사용자 이름
     */


    /**
     * request token 얻기
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);//요청체중에 해더 정보를 얻기(header:Authentication)
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {    //만약 token이 null이 아니고 접두사 Constants.TOKEN_PREFIX로 시작하면
            token = token.replace(Constants.TOKEN_PREFIX, "");//replace 메서드는 접두사TOKEN_PREFIX를 제거
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return Constants.LOGIN_TOKEN_KEY + uuid;//로그인 사용자 redis 키 + uuid
    }
}
