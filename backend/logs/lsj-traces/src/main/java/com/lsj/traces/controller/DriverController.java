package com.lsj.traces.controller;

import com.lsj.common.core.domain.AjaxResult;
import com.lsj.traces.domain.TraceLngLat;
import com.lsj.traces.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 기사 정보
 */
@RestController
@RequestMapping("/traces/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    /**
     * 기사 Id로 운송 정보를 얻기
     * @param driverId 기사 Id
     * @return 결과
     */
    @GetMapping("/listTransport/{driverId}")
    //@PathVariable: URL의 경로 매개 변수 deptId와 메서드 매개 변수 deptId를 바인딩함
    public AjaxResult listTransport(@PathVariable("driverId") String driverId) {
        return AjaxResult.success(driverService.listTransport(driverId));
    }

    /**
     * Database에 운송 정보 저장 및 업데이트
     * @param traceLngLat
     * @return
     */
    @PostMapping("/saveAndUpdateTransportInfoToDb")

    //JSON 데이터를 TraceLngLat 대상으로 변환
    public AjaxResult saveAndUpdateTransportInfoToDb(@RequestBody TraceLngLat traceLngLat) {
        //System.out.println("lat   " + traceLngLat);
        return AjaxResult.success(driverService.saveAndUpdateTransportInfoToDb(traceLngLat));
    }

}
