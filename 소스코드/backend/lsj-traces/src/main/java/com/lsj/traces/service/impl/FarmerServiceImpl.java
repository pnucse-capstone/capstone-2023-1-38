package com.lsj.traces.service.impl;

import com.lsj.common.core.domain.entity.SysDept;
import com.lsj.common.core.domain.entity.SysUser;
import com.lsj.system.mapper.SysDeptMapper;
import com.lsj.system.mapper.SysUserMapper;
import com.lsj.traces.mapper.TraceTransportMapper;
import com.lsj.traces.mapper.TraceUserCropsMapper;
import com.lsj.traces.service.FarmerService;
import com.lsj.traces.domain.TraceTransport;
import com.lsj.traces.domain.TraceUserCrops;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 농가 서비스층 실현
 */
@Service
public class FarmerServiceImpl implements FarmerService {
    @Autowired
    //traceCropsMapper 데이터베이스 맵퍼
    private TraceUserCropsMapper traceCropsMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private TraceTransportMapper traceTransportMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 농산물 추가
     * @param traceCrops 농산물
     * @return 성공하고 업로그
     */
    @Override
    //saveCrops 방법은 농산물 정보를 데이터베이스에 삽입하는 데 사용
    public int saveCrops(TraceUserCrops traceCrops) {
        return traceCropsMapper.insert(traceCrops);
    }

    /**
     * 사용자 이름으로 농산물 얻기
     * @param username 사용자 이름
     * @return 성공하고 업로그
     */
    @Override
    //getCropsByUsername 방법은 사용자 이름으로 농산물 정보를 얻는 데 사용
    public List<TraceUserCrops> getCropsByUsername(String username) {
        return traceCropsMapper.getCropsByUsername(username);
    }

    /**
     * 부서 Id로 기사 목록을 얻기
     * @param deptId 부서 Id
     * @return 성공하고 업로그
     */
    @Override
    //getAllDriverByDeptId 방법은 부서 Id로 기사 목록을 얻는 데 사용
    public List<SysUser> getAllDriverByDeptId(String deptId) {
        return sysUserMapper.selectUserByDeptId(deptId);
    }

    /**
     * 운송 추가
     * @param traceTransport 운송 정보
     * @return 성공하고 업로그
     */
    @Override
    //addTransport 방법은 운송 정보를 데이터베이스에 삽입하는 데 사용
    public int addTransport(TraceTransport traceTransport) {
        //농산물 ID 업데이트
        int i = traceCropsMapper.updateByCropsId(traceTransport.getCropsId());
        //운송 정보 추가
        //traceTransport는 데이터베이스로 업로드
        return traceTransportMapper.insertSelective(traceTransport);
    }

    /**
     * 부서 Id로 공장정보 얻기
     * @param deptId 부서 Id
     * @return 성공하고 업로그
     */
    @Override
    public List<SysDept> getFactoryByDeptId(Integer deptId) {
        return sysDeptMapper.getFactoryByDeptId(deptId);
    }


}


