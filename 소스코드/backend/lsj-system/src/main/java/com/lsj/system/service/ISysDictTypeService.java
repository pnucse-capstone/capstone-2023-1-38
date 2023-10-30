package com.lsj.system.service;

import java.util.List;

import com.lsj.common.core.domain.entity.SysDictData;

/**
 * 사전 업무층
 */
public interface ISysDictTypeService {

    /**
     * 사전 유형으로 사전 데이터를 조회
     * @param dictType 사전 유형
     * @return 사전 데어터 집합 정보
     */
    public List<SysDictData> selectDictDataByType(String dictType);


}
