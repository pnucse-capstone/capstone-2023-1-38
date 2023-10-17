package com.lsj.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 데어터 권한 필터링 주석
 */
@Target(ElementType.METHOD)//주석은 메소드에만 적용
@Retention(RetentionPolicy.RUNTIME)//주석의 생명 주시를 정의(런타임시 유효함)
public @interface DataScope {
    /**
     * 부서 테이블 별칭
     */
    public String deptAlias() default "";
}


