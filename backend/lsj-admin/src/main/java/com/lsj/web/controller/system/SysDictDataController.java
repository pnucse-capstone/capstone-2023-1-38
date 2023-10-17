package com.lsj.web.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lsj.common.core.controller.BaseController;
import com.lsj.common.core.domain.AjaxResult;
import com.lsj.common.core.domain.entity.SysDictData;
import com.lsj.common.core.page.TableDataInfo;
import com.lsj.system.service.ISysDictDataService;
import com.lsj.system.service.ISysDictTypeService;

/**
 * 데이터 사전 정보
 */
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")//사용자 권한 체크
    @GetMapping("/list")//get 요청 처리, 요청 경로 설정
    public TableDataInfo list(SysDictData dictData) {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    /**
     * 사전 유형으로 사전 데이터 정보를 조회
     */
    @GetMapping(value = "/type/{dictType}")//메소드는 "dictType"동적 경로의 get 요청을 처리
    public AjaxResult dictType(@PathVariable String dictType) {
        return AjaxResult.success(dictTypeService.selectDictDataByType(dictType));//dictType로 사전 유형을 조회하고 응답 메시지 반납
    }
}






