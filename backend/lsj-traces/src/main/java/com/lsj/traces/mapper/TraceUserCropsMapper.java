package com.lsj.traces.mapper;

import com.lsj.traces.domain.TraceUserCrops;
import com.lsj.traces.domain.vo.CropsVo;

import java.util.List;

public interface TraceUserCropsMapper {

    //농산물 정보 삭제
    int deleteByPrimaryKey(Long id);

    //농산물 정보 입력
    int insert(TraceUserCrops record);

    //농산물 정보 입력  non-null
    int insertSelective(TraceUserCrops record);

    //농산물 정보 검색
    TraceUserCrops selectByPrimaryKey(Long id);

    //농산물 정보 업데이트  non-null
    int updateByPrimaryKeySelective(TraceUserCrops record);

    //농산물 정보 업데이트 all
    int updateByPrimaryKey(TraceUserCrops record);

    //사용자 ID에 따라 사용자의 농작물 정보 목록 조회
    List<TraceUserCrops> getCropsByUsername(String username);

    //농산물 ID에 따라 정보 업데이트
    int updateByCropsId(String cropsId);

    int updateMachingStatus(CropsVo cropsVo);

    /**
     * @param cropsId
     * @return
     */
    int updateProductWriteStatus(String cropsId);

    /**
     * @param cropsId
     * @return
     */
    int productOutFactory(String cropsId);
}