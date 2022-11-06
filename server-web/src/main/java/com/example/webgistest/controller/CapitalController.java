package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.Capital;
import com.example.webgistest.service.ICapitalService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * SourceController
 * 获取capital数据
 * @author wnm
 * @date 2020/9/20
 */
@ApiSort(value = 9)
@Api(tags = "9.获取capital表数据")
@Controller
@RequestMapping("capital")
public class CapitalController {

    @Autowired
    ICapitalService iCapitalService;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "1.向表插入数据", notes = "添加新数据到表中。")
    @ApiImplicitParam(name = "capital", value = "Capital数据表", required = true, dataTypeClass = Capital.class, paramType = "body")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Integer> insert(Capital capital) {
        return iCapitalService.insert(capital);
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "2.查询表中所有数据", notes = "查询表中所有数据。")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Capital>> allSources() {
        return iCapitalService.selectAll();
    }
}
