package com.lsj.traces.service.impl;

import com.lsj.traces.mapper.TraceTaskMapper;
import com.lsj.traces.mapper.TraceTransportMapper;
import com.lsj.traces.mapper.TraceUserCropsMapper;
import com.lsj.traces.service.ProductService;
import com.lsj.traces.domain.TraceTask;
import com.lsj.traces.domain.vo.CropsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 가공공장 실현
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private TraceTransportMapper traceTransportMapper;
    @Autowired
    private TraceTaskMapper traceTaskMapper;
    @Autowired
    private TraceUserCropsMapper traceUserCropsMapper;

    /**
     * 부서 Id로 농산물 조회
     * @param deptId
     * @return
     */
    @Override
    //농산물 목록 조회
    public List<CropsVo> queryCropsList(Integer deptId) {
        return traceTransportMapper.queryCropsList(deptId);
    }

    @Override
    //작업 정보 추가
    public int addTask(TraceTask traceTask) {
        return traceTaskMapper.insertSelective(traceTask);
    }

    @Override
    //농산물 상태 업데이트
    public int updateMachingStatus(CropsVo cropsVo) {
        return traceUserCropsMapper.updateMachingStatus(cropsVo);
    }

    /**
     * 농산물 Id로 테스크 조회
     * @param cropsId 농산물 Id
     * @return
     */
    @Override
    public TraceTask queryTaskByCropsId(String cropsId) {
        return traceTaskMapper.queryTaskByCropsId(cropsId);
    }

    /**
     * 제품 기록 상태 업데이트
     * @param cropsId 농산물 Id
     * @return
     */
    @Override
    public int updateProductWriteStatus(String cropsId) {
        return traceUserCropsMapper.updateProductWriteStatus(cropsId);
    }

    /**
     * 테스크 조회
     * @return 결과
     */
    @Override
    public List<TraceTask> queryTaskList() {
        return traceTaskMapper.queryTaskList();
    }

    /**
     * 농산물 Id 따라 제품 출고로 설정
     * @param cropsId 농산물 Id
     * @return 결과
     */
    @Override
    public int productOutFactory(String cropsId) {
        return traceUserCropsMapper.productOutFactory(cropsId);
    }
}
