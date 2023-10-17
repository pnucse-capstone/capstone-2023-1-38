package com.lsj.web.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.lsj.common.config.lsjConfig;
import com.lsj.common.core.domain.AjaxResult;
import com.lsj.common.utils.file.FileUploadUtils;
import com.lsj.framework.config.ServerConfig;

/**
 * 통용 요청 처리
 */
@RestController
public class CommonController {

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 통용 업로드 요청
     */
    @PostMapping("/common/upload") //메소드는 post 요청을 처리
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // 파일 업로드 경로
            String filePath = lsjConfig.getUploadPath();//파일 업로드 경로를 lsjConfig 클래스에서 얻기
            // 업로드하고 새 파일 이름을 반납
            String fileName = FileUploadUtils.upload(filePath, file);//업로드된 파일을 지정된 경로에 저장하고 새 파일 이름을 반납
            String url = serverConfig.getUrl() + fileName;//파일의 URL을 생성
            AjaxResult ajax = AjaxResult.success();//AjaxResult 객체를 생성하고 상태 코드를 200으로 설정
            ajax.put("fileName", fileName);//fileName과 업로드된 파일의 새 이름을 추가
            ajax.put("url", url);// "url"과 파일의 url을 추가
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
