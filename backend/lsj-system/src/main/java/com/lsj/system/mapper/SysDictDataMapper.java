package com.lsj.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.lsj.common.core.domain.entity.SysDictData;

/**
 * 사전 테이블 데이터층
 */
public interface SysDictDataMapper {
    /**
     * 조건에 따라 페이징으로 사전 데이터 조회
     * @param dictData 사전 데이터 정보
     * @return 사전 데이터 집합 정보
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * 사전 유형으로 사전 데어터를 조회
     * @param dictType 사전 유형
     * @return 사전 데어터 집합 정보
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 사전 유형 및 사전 키값으로 사전 데이터 정보 조회
     * @param dictType  사전 유형
     * @param dictValue 사전 키값
     * @return 사전 태그
     */
    public String selectDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    /**
     * 사전 데이터 ID로 정보 조회
     * @param dictCode 사전 데이터 ID
     * @return 사전 데이터
     */
    public SysDictData selectDictDataById(Long dictCode);

    /**
     * 사전 데이터 조회
     * @param dictType 사전 유형
     * @return 사전 데이터
     */
    public int countDictDataByType(String dictType);

    /**
     * 사전 ID로 사전 데어터 정보를 삭제
     * @param dictCode 사전 데어터 ID
     * @return 결과
     */
    public int deleteDictDataById(Long dictCode);

    /**
     * 여러 사전 데이터 정보를 삭제
     * @param dictCodes 삭제할 사전 데이터 ID
     * @return 결과
     */
    public int deleteDictDataByIds(Long[] dictCodes);

    /**
     * 사전 데이터 정보를 추가
     * @param dictData 사전 데이터 정보
     * @return 결과
     */
    public int insertDictData(SysDictData dictData);

    /**
     * 사전 데이터 정보 수정
     * @param dictData 사전 데이터 정보
     * @return 결과
     */
    public int updateDictData(SysDictData dictData);

    /**
     * 동기식으로 사전 유형 수정
     * @param oldDictType 원래 사전 유형
     * @param newDictType 뉴 사전 유형
     * @return
     */
    public int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);
}
