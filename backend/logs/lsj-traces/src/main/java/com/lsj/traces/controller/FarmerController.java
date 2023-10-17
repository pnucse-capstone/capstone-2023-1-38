package com.lsj.traces.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.lsj.common.annotation.RepeatSubmit;
import com.lsj.common.core.controller.BaseController;
import com.lsj.common.core.domain.AjaxResult;
import com.lsj.common.core.domain.entity.SysUser;
import com.lsj.common.utils.SecurityUtils;
import com.lsj.traces.domain.TraceTransport;
import com.lsj.traces.domain.TraceUserCrops;
import com.lsj.traces.domain.vo.CropsImageVo;
import com.lsj.traces.service.FarmerService;
import com.lsj.traces.utils.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;

/**
 * 농가
 */
@RestController
@RequestMapping("/traces/farmer")
public class FarmerController extends BaseController {

    @Value("${fdfs.address}")
    private String address;
    @Autowired
    private FarmerService farmerService;
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 운송 추가
     * @param traceTransport
     * @return 운송정보 성공적인 추가하면 반환
     */
    @PostMapping("/addTransport")
    //addTransport 방법은 POST 요청을 수신하여 운송 정보를 추가하는 데 사용
    //JSON 데이터를 TraceTransport 대상으로 변환
    public AjaxResult addTransport(@RequestBody TraceTransport traceTransport){
        return AjaxResult.success(farmerService.addTransport(traceTransport));
    }

    /**
     * 부서 Id로 기사 목록 얻기
     * @param deptId 부서Id
     * @return 기사 목록 성공적인 획득하면 반환
     */
    @GetMapping("/getAllDriverByDeptId/{deptId}")
    //GET 요청을 수신하여 부서 ID에 따라 모든 운전자 정보를 획득하는 데 사용
    //@PathVariable: URL의 경로 매개 변수 deptId와 메서드 매개 변수 deptId를 바인딩함
    public AjaxResult getAllDriverByDeptId(@PathVariable("deptId") String deptId){
        //부서 ID를 매개 변수로 메서드에 전달하고 기사 정보가 포함된 SysUser 개체 목록을 반환
        List<SysUser> list = farmerService.getAllDriverByDeptId(deptId);
        return AjaxResult.success(list);
    }

    /**
     * 부서 ID로 공장정보 얻기
     * @param deptId 부서 ID
     * @return 공장정보 성공젹인 확득하면 반환
     */
    @GetMapping("/getFactoryByDeptId/{deptId}")
    //getFactoryByDeptId 방법은 GET 요청 접수 시 사용되며, 부서 ID에 따라 공장 정보 획득
    ////@PathVariable: URL의 경로 매개 변수 deptId와 메서드 매개 변수 deptId를 바인딩함
    public AjaxResult getFactoryByDeptId(@PathVariable("deptId") Integer deptId){
        return AjaxResult.success(farmerService.getFactoryByDeptId(deptId));
    }


    /**
     * 농산물 사진 업로그
     * @param cropsImageVo 농산물 사진
     * @return
     * @throws FileNotFoundException
     */
    @PostMapping("imageUpload") //메소드는 post 요청을 처리
    public AjaxResult imageUpload(@RequestBody CropsImageVo cropsImageVo) throws FileNotFoundException {
        String imageBase64 = cropsImageVo.getImageBase64().replace("data:image/jpeg;base64,", "");
        // Base64로 인코딩된 그림 데이터를 파일로 변환
        File file = Base64Util.base64ConvertFile(imageBase64);
        //사진 업로그하고 JPG파일 생선
        StorePath storePath = this.fastFileStorageClient.uploadImageAndCrtThumbImage(new FileInputStream(file),file.length(),"jpg",null);
        //저장 경로의 전체 경로를 address 서버 주소와 연결하고 성공 정보를 반환
        return AjaxResult.success(address+storePath.getFullPath());
    }

    /**
     * 농산물 추가
     * @param traceCrops 농산물
     * @return 농산물 정보 추사 성공하면 반환
     */
    @PostMapping("/saveCrops")
    //saveCrops 방법은 POST 요청을 수신하여 농산물 정보를 저장
    //JSON 데이터를 TraceUserCrops 대상으로 변환
    public AjaxResult saveCrops(@RequestBody TraceUserCrops traceCrops) {
        //농산물 정보의 사용자 계정 식별
        traceCrops.setUsername(SecurityUtils.getUsername());
        return AjaxResult.success(farmerService.saveCrops(traceCrops));
    }

    /**
     * 사용자 이름으로 농산물 얻기
     * @param username 사용자 이름
     * @return 사용자 이름으로 농산물 성공적인 얻으면 반환
     */
    @GetMapping("/getCropsByUsername/{username}")
    //getCropsByUsername방법은 GET 요청을 수신하여 사용자 이름에 따라 사용자의 농산물 정보를 얻는 데 사용
    public AjaxResult getCropsByUsername(@PathVariable("username") String username) {
        return AjaxResult.success(farmerService.getCropsByUsername(username));
    }

}
