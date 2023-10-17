package com.lsj.web.controller.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.code.kaptcha.Producer;
import com.lsj.common.constant.Constants;
import com.lsj.common.core.domain.AjaxResult;
import com.lsj.common.core.redis.RedisCache;
import com.lsj.common.utils.sign.Base64;
import com.lsj.common.utils.uuid.IdUtils;

/**
 * 인증번호 조작 처리
 */
@RestController
//@Controller와 @ResponseBody 두 주석을 함께 사용하는 간편한 방법
//클래스가 RESTful 스타일의 컨트롤러임을 나타내고 모든 처리 메서드가 반환 값을 JSON 또는 XML 형식으로 바로 클라이언트에게 전송하는데 사용
public class CaptchaController {
    @Resource(name = "captchaProducer")//captchaProducer 빈을 주입
    private Producer captchaProducer;//인증코드 제작

    @Resource(name = "captchaProducerMath")//captchaProducerMath 빈을 주입
    private Producer captchaProducerMath;//인증코드 제작자 制作 数学计算 的验证码

    @Autowired //Spring 컨테이너 내의 빈을 자동으로 주입
    private RedisCache redisCache;//redis 캐시

    @Value("${lsj.captchaType}") // application.yml 등)에서 설정 값을 읽어와서 빈에 필드로 주입
    private String captchaType;//인증코드 유형

    /**
     * 인증 코드 생성
     */
    @GetMapping("/captchaImage")//메소드는 captchaImage경로의 get 요청 처리
    public AjaxResult getCode(HttpServletResponse response) throws IOException { //메서드는 인증코드를 생성하고 인증코드 관련된 정보를 반납
        //인증코드 정보 저장
        String uuid = IdUtils.simpleUUID();//uuid식별자 생성
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;//verifyKey: 캐시 키 = CAPTCHA_CODE_KEY 상수 + uuid

        String capStr = null;//인증코드의 문제 초기화
        String code = null;//인증코드의 정답 초기화
        BufferedImage image = null;//인증코드의 그림 초기화

        //인증코드 생성
        if ("math".equals(captchaType)) { //구성 파일의 captchaType 유형이 수학인지 확인
            String capText = captchaProducerMath.createText();//인증코드 문자 생성(1+2=?@3)
            capStr = capText.substring(0, capText.lastIndexOf("@"));//문제
            code = capText.substring(capText.lastIndexOf("@") + 1);//정답
            image = captchaProducerMath.createImage(capStr);//문제 따라 인증코드 이미지 생성
        } else if ("char".equals(captchaType)) { //구성 파일의 captchaType 유형이 문자인지 확인
            capStr = code = captchaProducer.createText();//텍스트 유형 인증코드 생성
            image = captchaProducer.createImage(capStr);//인증코드 이미지 생성
        }

        /**
         * 인증코드 캐시 설정
         * 인증코드 키, 인증코드 값, 인증코드 유효 기간(분), 시간 단위
         * @param verifyKey 캐시의 값, 인증코드의 유일 식별자
         * @param code 캐시의 값, 인증코드의 정답
         * @param Constants.CAPTCHA_EXPIRATION 캐시된 유효 기간
         * @param TimeUnit.MINUTES 시간 단위
         */
        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        //생성된 인증코드의 이미지를 출력 스트림에 작성
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        //org.springframework.util.FastByteArrayOutputStream은 Spring Framework에서 제공하는 빠른 바이트 배열 출력 스트림 클래스

        try {
            ImageIO.write(image, "jpg", os);//이미지 개체를 JPG 형식으로 OS 출력 스트림에 작성
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());//예외가 발생하면 AjaxResult.error() 메서드로 예외 정보 응답 반납
        }

        //프런트 엔드에세 uuid, img(base64의 인증코드, code200, 성공 msg)
        AjaxResult ajax = AjaxResult.success();//성공을 표시하는 AjaxResult 객체를 생성
        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));//os.toByteArray() 바이트 배열을 Base64 인코딩 형식으로 변환하여 얻기
        return ajax;
    }
}
