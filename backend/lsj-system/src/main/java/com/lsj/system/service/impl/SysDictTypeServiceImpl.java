package com.lsj.system.service.impl;

import java.util.List;
import javax.annotation.PostConstruct;

import com.lsj.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lsj.common.core.domain.entity.SysDictData;
import com.lsj.common.core.domain.entity.SysDictType;
import com.lsj.common.utils.DictUtils;
import com.lsj.common.utils.StringUtils;
import com.lsj.system.mapper.SysDictDataMapper;
import com.lsj.system.mapper.SysDictTypeMapper;

/**
 * 사전 업무층 처리
 */
@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService {
    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    @PostConstruct
    public void init() {
        List<SysDictType> dictTypeList = dictTypeMapper.selectDictTypeAll();
        for (SysDictType dictType : dictTypeList) {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(dictType.getDictType());
            DictUtils.setDictCache(dictType.getDictType(), dictDatas);
        }
    }

    /**
     * 사전 유형으로 사전 데이터를 조회
     *
     * @param dictType 사전 유형
     * @return 사전 데어터 집합 정보
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);//dictType로 캐시중에서 사전 데이터 목록을 얻기
        if (StringUtils.isNotEmpty(dictDatas)) { //사전 데이터 있으면 데이터를 반납
            return dictDatas;
        }
        dictDatas = dictDataMapper.selectDictDataByType(dictType);//캐시중에 없으면 데이터베이스에서 사전 데어터를 조회
        if (StringUtils.isNotEmpty(dictDatas)) { //사전 데이터 있으면
            DictUtils.setDictCache(dictType, dictDatas);//사전 데어터를 사전 캐시중에 저장
            return dictDatas;//사전 데어터를 반납
        }
        return null;
    }
}
