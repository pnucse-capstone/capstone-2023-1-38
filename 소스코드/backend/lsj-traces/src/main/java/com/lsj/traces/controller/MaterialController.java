package com.lsj.traces.controller;

import com.lsj.common.core.domain.AjaxResult;
import com.lsj.traces.domain.TraceTransport;
import com.lsj.traces.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 원료공장
 */
@RestController
@RequestMapping("/traces/material")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    @GetMapping("/listCrops/{deptId}")
    //@PathVariable: URL의 경로 매개 변수 deptId와 메서드 매개 변수 deptId를 바인딩함
    public AjaxResult listCrops(@PathVariable("deptId") String deptId){
        return AjaxResult.success(materialService.listCrops(deptId));
    }

    /**
     * 농산물 Id로 출고설지
     * @param cropsId 농산물 Id
     * @return 결과
     */
    @GetMapping("/changeInToOut/{cropsId}")
    //@PathVariable: URL의 경로 매개 변수 cropsId와 메서드 매개 변수 deptId를 바인딩함
    public AjaxResult changeInToOut(@PathVariable("cropsId") String cropsId){
        return AjaxResult.success(materialService.changeInToOut(cropsId));
    }

    /**
     * 운송 추가
     * @param traceTransport
     * @return 운송정보 성공적인 추가하면 반환
     */
    @PostMapping("/addTransport")
    //JSON 데이터를 TraceTransport 대상으로 변환
    public AjaxResult addTransport(@RequestBody TraceTransport traceTransport){
        return AjaxResult.success(materialService.addTransport(traceTransport));
    }
}
