package com.lsj.framework.aspectj;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.lsj.common.annotation.DataScope;
import com.lsj.common.core.domain.BaseEntity;
import com.lsj.common.core.domain.entity.SysRole;
import com.lsj.common.core.domain.entity.SysUser;
import com.lsj.common.core.domain.model.LoginUser;
import com.lsj.common.utils.ServletUtils;
import com.lsj.common.utils.StringUtils;
import com.lsj.common.utils.spring.SpringUtils;
import com.lsj.framework.web.service.TokenService;

/**
 * 데이터 필터링
 */
@Aspect //애스펙트 클래스 정의
@Component //자동으로 클래스를 spring에 스캔하여 해당 Bean 인스턴스를 생성하고 종속성 주입(@Autowired, @Resource 등)을 통해 다른 구성 요소에서 사용
public class DataScopeAspect {
    /**
     * 모든 데이터 권한
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 스스로 정의 데이터 권한
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 본 부서 데이터 권한
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 데이터 권한 필터링 키워드
     */
    public static final String DATA_SCOPE = "dataScope";

    /**
     * @Pointcut Spring AOP 포인트컷을 정의, 포인트컷은 코드에서 지정한 조인포인트(메서드)들을 나타낸다. 프로그램이 조인포인트에 도달하면 Aspect의 로직이 실행
     * @annotation DataScope 주석이 있는 메소드를 매핑
     */
    //포인트 컷 정의
    @Pointcut("@annotation(com.lsj.common.annotation.DataScope)")//定义一个切点，该切点将在带有@DataScope注解的方法上执行切面逻辑
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()") //포인트컷 메소드dataScopePointCut() 실행하기전에 먼저 실행
    public void doBefore(JoinPoint point) throws Throwable {
        handleDataScope(point);//데이터 권한 처리
    }

    /**
     * 데이터 범위 처리
     * @param joinPoint 어드바이스가 적용될 수 있는 위치
     */
    protected void handleDataScope(final JoinPoint joinPoint) {
        //주석을 얻기
        DataScope controllerDataScope = getAnnotationLog(joinPoint);//JoinPoint 객체의 DataScope 주석 객체를 얻기
        if (controllerDataScope == null) { //주석 객체 null이면 해당 메서드에 데이터 권한 처리를 위한 주석 없으며 실행을 중단하고 반납
            return;
        }
        //현재 로그인한 사용자 얻기
        LoginUser loginUser = SpringUtils.getBean(TokenService.class).getLoginUser(ServletUtils.getRequest());
        if (StringUtils.isNotNull(loginUser)) { //사용자 있으면
            SysUser currentUser = loginUser.getUser();//로그인 사용자 정보에서 SysUser 객체를 얻기

            if (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin()) { //사용자가 null아니고 관리자아닌 경우에
                //데이터 범위 필터링 메소드를 호출
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias());
            }
        }
    }

    /**
     * 데이터 범위 필터링
     *
     * @param joinPoint 조인포인트
     * @param user      사용자
     * @param deptAlias 부서 별칭
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String deptAlias) {
        StringBuilder sqlString = new StringBuilder();

        for (SysRole role : user.getRoles()) { //사용자 역할 목록 방문
            String dataScope = role.getDataScope();//역할 설정된 데이터 권한 범위 얻기 (1:모든 데이터 권한; 2:사용자 지정 데이터 권한; 3:본 부서 데이터 권한)

            if (DATA_SCOPE_ALL.equals(dataScope)) {//권한은 모든 데이터 권한이면 데이터 필터링하지 않는다
                sqlString = new StringBuilder();//필터링 필요 없으니 빈 문자열로 설정
                break;

            } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {//권한은 스스로 정의 데이터 권한이면 역할 및 부서 관련표로 데이터 필터링한다
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias,
                        role.getRoleId()));

            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {//권한은 부서 데이터 권한이면 현재 사용자가 있는 부서 데이터만 조회
                sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
            }
        }

        //sqlString에 따라 sql 조회 매개변수에 추가
        if (StringUtils.isNotBlank(sqlString.toString())) { //sqlString null아니면
            Object params = joinPoint.getArgs()[0];//조인포인트에서 메소드 매개변수 배열을 얻기
            if (StringUtils.isNotNull(params) && params instanceof BaseEntity) { //params 유형은 BaseEntity유형인지 확인
                BaseEntity baseEntity = (BaseEntity) params;//BaseEntity유형으로 변환
                //sqlString을 "AND"로 추가하고 키값쌍으로 baseEntity 객체에 추가
                baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");//AND (dept_id = 1 OR dept_id = 2)
            }
        }
    }

    /**
     * 주석이 있는지 여부, 있으면 가져오기
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();//연결점의 서명 정보를 얻기,(메서드의 이름, 접근 제어자, 반납 타입 등이 포함)
        MethodSignature methodSignature = (MethodSignature) signature;//MethodSignature 유형으로 변환
        Method method = methodSignature.getMethod();//조인포인트(JoinPoint)을 나타내는 메서드 객체를 얻기

        if (method != null) { //메소드 null 아니면
            return method.getAnnotation(DataScope.class);//해당 메서드에 있는 DataScope 주석 객체를 얻기
        }
        return null;
    }
}
