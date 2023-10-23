package com.lsj.traces.service.impl;

import com.lsj.system.mapper.SysUserMapper;
import com.lsj.traces.domain.TraceLngLat;
import com.lsj.traces.domain.TraceTransport;
import com.lsj.traces.mapper.TraceLngLatMapper;
import com.lsj.traces.mapper.TraceTransportMapper;
import com.lsj.traces.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 기사 서비스 층 실현
 */
@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private TraceTransportMapper traceTransportMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private TraceLngLatMapper traceLngLatMapper;

    /**
     * 기사 Id로 운송 정보를 얻기
     *
     * @param driverId 기사 Id
     * @return 결과
     */
    @Override
    public List<TraceTransport> listTransport(String driverId) {
        return traceTransportMapper.listTransport(driverId);
    }

    /**
     * 保存并更新跟踪信息
     * @param traceLngLat
     * @return
     */
    @Override
    public int saveAndUpdateTransportInfoToDb(TraceLngLat traceLngLat) {
        TraceTransport traceTransport = new TraceTransport();
        //출고 상태 설정
        traceTransport.setOutFactoryStatus(traceLngLat.getOutFactoryStatus());
        //设置Id
        traceTransport.setId(traceLngLat.getId());
        //농산물 Id 설정
        traceTransport.setCropsId(traceLngLat.getCropsId());
        //设置状态
        traceTransport.setStatus(traceLngLat.getStatus());
        //농산물 Id따라 운송 정보 업데이트

        traceLngLatMapper.insertSelective(traceLngLat);
        //농산물 Id로 운송 정보 업데이트
        return traceTransportMapper.updateByCropsId(traceTransport);
    }

}
