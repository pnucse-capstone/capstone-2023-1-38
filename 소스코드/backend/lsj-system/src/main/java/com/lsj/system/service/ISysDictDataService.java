package com.lsj.system.service;

import java.util.List;

import com.lsj.common.core.domain.entity.SysDictData;

/**
 * 사전 업무층
 */
public interface ISysDictDataService {
    /**
     * 조건에 따라 페이징으로 사전 데이터 조회
     *
     * @param dictData 사전 데이터 정보
     * @return 사전 데이터 집합 정보
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

}
