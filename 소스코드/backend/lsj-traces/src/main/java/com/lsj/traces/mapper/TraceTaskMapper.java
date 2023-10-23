package com.lsj.traces.mapper;

import com.lsj.traces.domain.TraceTask;

import java.util.List;

public interface TraceTaskMapper {

    int deleteByPrimaryKey(String taskId);

    int insert(TraceTask record);

    int insertSelective(TraceTask record);

    TraceTask selectByPrimaryKey(String taskId);

    int updateByPrimaryKeySelective(TraceTask record);

    int updateByPrimaryKey(TraceTask record);

    /**
     * 농산물 Id로 任务 조회
     * @param cropsId
     * @return
     */
    TraceTask queryTaskByCropsId(String cropsId);

    /**
     * task 조회
     * @return 결과
     */
    List<TraceTask> queryTaskList();
}