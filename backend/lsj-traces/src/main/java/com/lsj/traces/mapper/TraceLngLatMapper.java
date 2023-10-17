package com.lsj.traces.mapper;

import com.lsj.traces.domain.TraceLngLat;

public interface TraceLngLatMapper {

    int deleteByPrimaryKey(Long id);

    int insert(TraceLngLat record);

    int insertSelective(TraceLngLat record);

    TraceLngLat selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TraceLngLat record);

    int updateByPrimaryKey(TraceLngLat record);
}