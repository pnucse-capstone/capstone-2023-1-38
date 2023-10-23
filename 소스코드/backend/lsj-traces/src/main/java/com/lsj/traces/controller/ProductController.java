package com.lsj.traces.controller;

import com.lsj.common.core.domain.AjaxResult;
import com.lsj.traces.domain.TraceTask;
import com.lsj.traces.domain.vo.CropsVo;
import com.lsj.traces.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 가공공장
 */
@RestController
@RequestMapping("/traces/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 부서 Id로 농산물 조회
     * @param deptId
     * @return
     */
    @GetMapping("/queryCropsList/{deptId}")
    public AjaxResult queryCropsList(@PathVariable Integer deptId){
        return AjaxResult.success(productService.queryCropsList(deptId));
    }

    /**
     * 테스크 추가
     * @param traceTask
     * @return 테스크 추가 성공하면 반환
     */
    @PostMapping("/addTask")
    public AjaxResult addTask(@RequestBody TraceTask traceTask){
        return AjaxResult.success(productService.addTask(traceTask));
    }

    /**
     * 가공상태 업데이트
     * @param cropsVo
     * @return 상태 성공적인 업데이트하면 반환
     */
    @PostMapping("/updateMachingStatus")
    //JSON 데이터를 CropsVo 대상으로 변환
    public AjaxResult updateMachingStatus(@RequestBody CropsVo cropsVo){
        return AjaxResult.success(productService.updateMachingStatus(cropsVo));
    }

    /**
     * 농산물 Id로
     * @param cropsId
     * @return
     */
    @GetMapping("/queryTaskByCropsId/{cropsId}")
    //@PathVariable: URL의 경로 매개 변수 cropsId와 메서드 매개 변수 cropsId를 바인딩함
    public AjaxResult queryTaskByCropsId(@PathVariable("cropsId") String cropsId){
        return AjaxResult.success(productService.queryTaskByCropsId(cropsId));
    }

    /**
     * @param cropsId
     * @return
     */
    @GetMapping("/updateProductWriteStatus/{cropsId}")
    //@PathVariable: URL의 경로 매개 변수 cropsId와 메서드 매개 변수 cropsId를 바인딩함
    public AjaxResult updateProductWriteStatus(@PathVariable("cropsId") String cropsId){
        return AjaxResult.success(productService.updateProductWriteStatus(cropsId));
    }

    /**
     * 테스크 조회
     * @return 결과
     */
    @GetMapping("/queryTaskList")
    public AjaxResult queryTaskList(){
        return AjaxResult.success(productService.queryTaskList());
    }

    /**
     * @param cropsId
     * @return
     */
    @GetMapping("/productOutFactory/{cropsId}")
    //@PathVariable: URL의 경로 매개 변수 cropsId와 메서드 매개 변수 cropsId를 바인딩함
    public AjaxResult productOutFactory(@PathVariable("cropsId") String cropsId){
        return AjaxResult.success(productService.productOutFactory(cropsId));
    }
}
