package com.lsj.traces.service.impl;

import com.lsj.traces.mapper.TraceTransportMapper;
import com.lsj.traces.service.RetailerService;
import com.lsj.traces.domain.TraceTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 판매자 서버증 실현
 */
@Service
public class RetailerServiceImpl implements RetailerService {
    @Autowired
    private TraceTransportMapper traceTransportMapper;

    /**
     * 운성 정보 따라 접수 상태 설정
     * @param traceTransport
     * @return
     */
    @Override
    public int updateReceiveStatus(TraceTransport traceTransport) {
        return traceTransportMapper.updateReceiveStatus(traceTransport);
    }
}
