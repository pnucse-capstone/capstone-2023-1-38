package com.lsj.traces.service;

import com.lsj.traces.domain.TraceTransport;

/**
 * 판매자 업무층
 */
public interface RetailerService {
    /**
     * 운송 정보 따라 접수 상태 설정
     * @param traceTransport
     * @return
     */
    int updateReceiveStatus(TraceTransport traceTransport);
}
