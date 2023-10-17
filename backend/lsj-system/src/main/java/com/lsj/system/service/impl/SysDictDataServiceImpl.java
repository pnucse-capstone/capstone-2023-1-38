package com.lsj.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lsj.common.core.domain.entity.SysDictData;
import com.lsj.common.utils.DictUtils;
import com.lsj.system.mapper.SysDictDataMapper;
import com.lsj.system.service.ISysDictDataService;

/**
 * 사전 업무층 처리
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService {
    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 조건에 따라 페이징으로 사전 데이터 조회
     *
     * @param dictData 사전 데이터 정보
     * @return 사전 데이터 집합 정보
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        return dictDataMapper.selectDictDataList(dictData);
    }

}
