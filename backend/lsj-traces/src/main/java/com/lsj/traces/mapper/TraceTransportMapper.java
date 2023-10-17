package com.lsj.traces.mapper;

import com.lsj.traces.domain.TraceTransport;
import com.lsj.traces.domain.vo.CropsVo;

import java.util.List;

public interface TraceTransportMapper {

    int insertSelective(TraceTransport record);

    /**
     * 기사 Id로 운송 정보를 얻기
     *
     * @param driverId 기사 Id
     * @return 결과
     */
    List<TraceTransport> listTransport(String driverId);

    /**
     * 농산물 Id따라 운송 정보 업데이트
     * @param traceTransport
     * @return
     */
    int updateByCropsId(TraceTransport traceTransport);

    List<TraceTransport> listCrops(String deptId);

    /**
     * 농산물 Id따라 设置出厂状态为出库
     *
     * @param cropsId 농산물 Id
     * @return 결과
     */
    int changeInToOut(String cropsId);

    /**
     * 부서 Id로 농산물 조회
     * @param deptId
     * @return
     */
    List<CropsVo> queryCropsList(Integer deptId);

    int updateReceiveStatus(TraceTransport traceTransport);
}