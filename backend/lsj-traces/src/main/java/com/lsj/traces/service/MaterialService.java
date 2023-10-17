package com.lsj.traces.service;

import com.lsj.traces.domain.TraceTransport;

import java.util.List;

//원료공장 업무층
public interface MaterialService {
    /**
     * 부서 Id로 운송 정보 목록 얻기
     * @param deptId
     * @return
     */
    List<TraceTransport> listCrops(String deptId);

    /**
     * 농산물 Id로 출고설지
     * @param cropsId 농산물 Id
     * @return 결과
     */
    int changeInToOut(String cropsId);

    /**
     * 운송 추가
     * @param traceTransport
     * @return 운송정보 성공적인 추가하면 반환
     */
    int addTransport(TraceTransport traceTransport);
}
