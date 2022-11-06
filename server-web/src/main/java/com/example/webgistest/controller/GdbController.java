package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.GdbLayerInfo;
import com.example.webgistest.service.IGdbService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@ApiSort(value = 11)
@Api(tags = "11.GDAL读取gdb数据")
@Controller
@RequestMapping("gdb")
public class GdbController {

    @Autowired
    IGdbService iGdbService;

    /**
     * 获取图层信息
     * @return
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "1.获取图层信息", notes = "读取gdb获取图层信息。")
    @RequestMapping(value = "/layers", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<GdbLayerInfo>> getLayers(){
        return iGdbService.getLayers();
    }

    /**
     * 计算工程治理长度和河岸治理长度
     * @return
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "2.计算工程长度", notes = "计算工程治理长度和河岸治理长度。")
    @RequestMapping(value = "/calculate", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Map>> getCalculate(){
        String file = "data/testdata.gdb";
        return iGdbService.getResult();
    }
}
