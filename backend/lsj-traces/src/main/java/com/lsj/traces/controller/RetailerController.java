package com.lsj.traces.controller;

import com.lsj.common.core.domain.AjaxResult;
import com.lsj.traces.domain.TraceTransport;
import com.lsj.traces.service.RetailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 판매자
 */
@RestController
@RequestMapping("/traces/retailer")
public class RetailerController {

    @Autowired
    private RetailerService retailerService;

    /**
     * 운송 정보 따라 접수 상태 설정
     * @param traceTransport
     * @return 작업이 성공적으로 완료되었으며 작업 결과를 AjaxResult에 캡슐화하여 클라이언트에 반환했습니다.
     */
    @PostMapping("/updateReceiveStatus")
    //JSON 데이터를 TraceTransport 대상으로 변환
    public AjaxResult updateReceiveStatus(@RequestBody TraceTransport traceTransport) {
        //작업이 성공적으로 완료되었으며 작업 결과를 AjaxResult에 캡슐화하여 클라이언트에 반환
        return AjaxResult.success(retailerService.updateReceiveStatus(traceTransport));
    }
}
