package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.University;
import com.example.webgistest.service.IUniversityService;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * SourceController
 *综合例子：获取大学数据
 * @author wnm
 * @date 2020/9/20
 */
@ApiSort(value = 10)
@Api(tags = "10.获取大学数据")
@Controller
@RequestMapping("university")
public class UniversityController {
    @Autowired
    IUniversityService iUniversityService;

    @ApiOperation(value = "1.获取指定学校数据", notes = "根据学校名称、等级、所在省份、类型获取学校信息.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "学校名称", dataTypeClass = String.class),
            @ApiImplicitParam(name = "level", value = "学校等级", dataTypeClass = String.class),
            @ApiImplicitParam(name = "province", value = "学校所在省份", dataTypeClass = String.class),
            @ApiImplicitParam(name = "type", value = "学校类型", dataTypeClass = String.class)
    })
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<University>> getUniversity(String name, String level, String province, String type) {
        return iUniversityService.getUniversity(name,level,province,type);
    }
}
