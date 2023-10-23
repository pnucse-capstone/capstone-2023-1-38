package com.lsj.common.core.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * spring redis Tools class
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})  //컴파일러에게 "unchecked" 와 "rawtypes" 형식의 경고를 무시하도록 지시한다
@Component //자동 실례와 다른 필요하신 곳에 주입할 수 있는 곳에 주입할 수 있다
public class RedisCache {
    @Autowired
    public RedisTemplate redisTemplate;
    //RedisTemplate는 Spring에서 제공하는 Redis를 조작하기 위한 템플릿 클래스이다

    /**
     * 개시 기본 객체: Integer、String、Entity 클래스 등
     * Cache 기본 객체: Integer、String、entity class
     * @param key   캐시 키
     * @param value 캐시 값
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);//ValueOperations 개체를 얻은 후 키 값 쌍을 설정
    }

    /**
     * 개시 기본 객체, Integer、String、Entity 클래스 등
     * Cache 기본 객체: Integer、String、entity class
     * @param key      캐시 키
     * @param value    캐시 값(로그인 정보)
     * @param timeout  시간
     * @param timeUnit 사간 단위
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        //opsForValue(): 문자열 형식 데이터를 조작하기 위한 ValueOperations 개체를 반납
        //set(key,value,timeout,timeUnit): 키값쌍을 Redis에 저장하고 만료 시간을 설정
    }

    /**
     * 유효 시간 설정
     *
     * @param key     Redis 키
     * @param timeout 타임아웃 주기
     * @return true=설정 성공；false=설정 실패
     */
    /**
     * 유효 시간 설정
     *
     * @param key     Redis 키
     * @param timeout 타임아웃 주기
     * @param unit    시간 단위
     * @return true=설정 성공；false=설정 실패
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 캐시의 기본 개체를 얻기
     *
     * @param key 캐시 키
     * @return 캐시 키 값은 해당하는 데이터
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 객체 하나 삭제
     *
     * @param key
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 객체 집합 삭제
     *
     * @param collection 여러 객체
     * @return
     */
    public long deleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * Cache 기본 개체 목록 얻기
     *
     * @param pattern 문자열 prefix
     * @return 객체 목록
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }
}



