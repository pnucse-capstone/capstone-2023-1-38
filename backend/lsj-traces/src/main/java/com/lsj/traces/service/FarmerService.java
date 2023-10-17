package com.lsj.traces.service;

import com.lsj.common.core.domain.entity.SysDept;
import com.lsj.common.core.domain.entity.SysUser;
import com.lsj.traces.domain.TraceTransport;
import com.lsj.traces.domain.TraceUserCrops;

import java.util.List;

//농가 업무층
public interface FarmerService {

    /**
     * 농산물 추가
     * @param traceCrops 농산물
     * @return 농산물 정보 추가 성공하면 반환
     */
    int saveCrops(TraceUserCrops traceCrops);

    /**
     * 사용자 이름으로 농산물 얻기
     * @param username 사용자 이름
     * @return 사용자 이름으로 농산물 성공적인 얻으면 반환
     */
    List<TraceUserCrops> getCropsByUsername(String username);

    /**
     * 부서 Id로 기사 목록 얻기
     * @param deptId 부서Id
     * @return 기사 목록 성공적인 획득하면 반환
     */
    List<SysUser> getAllDriverByDeptId(String deptId);

    /**
     * 운송 추가
     * @param traceTransport 운송정보
     * @return 운송정보 성공적인 추가하면 반환
     */
    int addTransport(TraceTransport traceTransport);

    /**
     * 부서 ID로 공장정보 얻기
     * @param deptId 부서 ID
     * @return 공장정보 성공젹인 확득하면 반환
     */
    List<SysDept> getFactoryByDeptId(Integer deptId);
}
