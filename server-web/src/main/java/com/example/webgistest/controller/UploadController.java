package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 文件上传 Controller
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-06 16:33
 */
@ApiSort(value = 6)
@Api(tags = "6.文件上传")
@RestController
public class UploadController {
    @Value("${spring.servlet.multipart.location}")
    private String fileTempPath;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "1.文件上传", notes = "文件上传.")
    @ApiImplicitParam(name = "file", value = "文件", required = true)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ServerResponse<String> upload(MultipartFile file) {
        if (file.isEmpty()) {
            return ServerResponse.createByErrorCodeMessage(400, "文件内容为空!");
        }
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        //获取文件前缀名
        String rawFileName = fileName.substring(0, fileName.lastIndexOf("."));
        //获取文件类型(后缀名)
        String[] strArray = fileName.split("\\.");
        String fileType = strArray[strArray.length - 1];
        String localFilePath = fileTempPath + "/" + rawFileName + "." + fileType;
        try {
            file.transferTo(new File(localFilePath));
            return ServerResponse.createBySuccess("文件上传成功！", localFilePath);
        } catch (IOException e) {
            return ServerResponse.createByErrorCodeMessage(500, "文件上传失败!");
        }
    }
}
