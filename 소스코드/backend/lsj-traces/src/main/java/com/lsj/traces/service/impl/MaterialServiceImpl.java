package com.lsj.traces.service.impl;

import com.lsj.traces.domain.TraceTransport;
import com.lsj.traces.mapper.TraceTransportMapper;
import com.lsj.traces.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 원료 서버증 실현
 */
@Service
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    private TraceTransportMapper traceTransportMapper;

    /**
     * 부서 Id로 운송 정보 목록 얻기
     * @param deptId
     * @return
     */
    @Override
    public List<TraceTransport> listCrops(String deptId) {
        return traceTransportMapper.listCrops(deptId);
    }

    /**
     * 농산물 Id따라 출고 상태로 설정
     * @param cropsId 농산물 Id
     * @return 결과
     */
    @Override
    public int changeInToOut(String cropsId) {
        return traceTransportMapper.changeInToOut(cropsId);
    }

    /**
     * 운송 정보 추가
     * @param traceTransport 운송 정보
     * @return
     */
    @Override
    public int addTransport(TraceTransport traceTransport) {
        return traceTransportMapper.insertSelective(traceTransport);
    }
}
