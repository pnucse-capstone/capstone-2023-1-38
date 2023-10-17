package com.lsj.common.utils;

import java.util.List;

import com.lsj.common.constant.Constants;
import com.lsj.common.core.domain.entity.SysDictData;
import com.lsj.common.core.redis.RedisCache;
import com.lsj.common.utils.spring.SpringUtils;

/**
 * 사전 도구 클래스
 */
public class DictUtils {

    /**
     * 사전 케시 설정
     *
     * @param key       매개 변수 키
     * @param dictDatas 사전 데이터 목록
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas) {
        SpringUtils.getBean(RedisCache.class).setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * 사전 캐시 얻기
     *
     * @param key 매개 변수 키
     * @return dictDatas 서전 데이터 목록
     */
    public static List<SysDictData> getDictCache(String key) {
        Object cacheObj = SpringUtils.getBean(RedisCache.class).getCacheObject(getCacheKey(key));
        //SpringUtils.getBean(RedisCache.class)：通过SpringUtils工具类获取名为RedisCache的缓存对象
        //getCacheObject(getCacheKey(key))：从缓存中用给定的键获取相关的对象

        if (StringUtils.isNotNull(cacheObj)) { //从缓存中获取的对象是否非空
            List<SysDictData> dictDatas = StringUtils.cast(cacheObj);
            return dictDatas;
        }
        return null;
    }

    /**
     * 캐시 키 설정
     *
     * @param configKey 매개 변수 키
     * @return 캐시 키
     */
    public static String getCacheKey(String configKey) {
        return Constants.SYS_DICT_KEY + configKey;
    }
}
