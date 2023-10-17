package com.lsj.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.lsj.common.core.domain.entity.SysDictType;

/**
 * 사전 테이블 데이터 층
 */
@Mapper
public interface SysDictTypeMapper {
    /**
     * 조건에 따라 페이징으로 사전 데이터 조회
     * @param dictType 사전 유형 정보
     * @return 사전 유형 집합 정보
     */
    public List<SysDictType> selectDictTypeList(SysDictType dictType);

    /**
     * 모든 사전 유형 조회
     * @return 사전 유형 집합 정보
     */
    public List<SysDictType> selectDictTypeAll();

    /**
     * 사전 유형 ID로 정보를 조회
     * @param dictId 사전 유형 ID
     * @return 사전 유형
     */
    public SysDictType selectDictTypeById(Long dictId);


    public SysDictType selectDictTypeByType(String dictType);


    public int deleteDictTypeById(Long dictId);

    /**
     * 여러 사전 유형 정보를 삭제
     *
     * @param dictIds 삭제할 사전 ID
     * @return 결과
     */
    public int deleteDictTypeByIds(Long[] dictIds);

    /**
     * 사전 유형 정보 추가
     *
     * @param dictType 사전 유형 정보
     * @return 결과
     */
    public int insertDictType(SysDictType dictType);

    /**
     * 사전 유형 정보 수정
     * @param dictType 사전 유형 정보
     * @return 결과
     */
    public int updateDictType(SysDictType dictType);

    /**
     * 사전 유형 유일한지 체크
     * @param dictType 사전 유형
     * @return 결과
     */
    public SysDictType checkDictTypeUnique(String dictType);
}
