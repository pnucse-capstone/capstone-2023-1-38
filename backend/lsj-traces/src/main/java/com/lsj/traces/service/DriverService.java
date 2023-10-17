package com.lsj.traces.service;

import com.lsj.traces.domain.TraceLngLat;
import com.lsj.traces.domain.TraceTransport;

import java.util.List;

//기사 업무층
public interface DriverService {
    /**
     * 기사 Id로 운송 정보를 얻기
     * @param driverId 기사 Idwoke
     * @return 결과
     */
    List<TraceTransport> listTransport(String driverId);

    /** 운송 정보를 저장 및 업데이트
     * @param traceLngLat
     * @return
     */
    int saveAndUpdateTransportInfoToDb(TraceLngLat traceLngLat);
}
