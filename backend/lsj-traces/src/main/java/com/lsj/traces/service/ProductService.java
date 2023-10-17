package com.lsj.traces.service;

import com.lsj.traces.domain.TraceTask;
import com.lsj.traces.domain.vo.CropsVo;

import java.util.List;

/**
 * 가공 업무층
 */
public interface ProductService {
    /**
     * 부서 Id로 농산물 조회
     * @param deptId
     * @return
     */
    List<CropsVo> queryCropsList(Integer deptId);

    /**
     * 테스크 추가
     * @param traceTask
     * @return 테스크 추가 성공하면 반환
     */
    int addTask(TraceTask traceTask);

    /**
     * 가공상태 업데이트
     * @param cropsVo
     * @return 상태 성공적인 업데이트하면 반환
     */
    int updateMachingStatus(CropsVo cropsVo);

    /**
     * 농산물 Id로
     * @param cropsId
     * @return
     */
    TraceTask queryTaskByCropsId(String cropsId);

    /**
     * @param cropsId
     * @return
     */
    int updateProductWriteStatus(String cropsId);

    /**
     * task 조회
     * @return 결과
     */
    List<TraceTask> queryTaskList();

    /**
     * @param cropsId
     * @return
     */
    int productOutFactory(String cropsId);
}
