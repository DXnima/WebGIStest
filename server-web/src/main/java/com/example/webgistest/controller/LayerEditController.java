package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.service.ILayerEditService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
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
import java.util.Map;

/**
 * SourceController
 * 编辑空间数据测试
 * @author wnm
 * @date 2020/9/20
 */
@ApiSort(value = 10)
@Api(tags = "10.编辑空间数据测试")
@Controller
@RequestMapping("layer_edit")
public class LayerEditController {
    @Autowired
    ILayerEditService iLayerEditService;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "1.按名称查询", notes = "按名称查询编辑表中的数据。")
    @ApiImplicitParam(name = "name", value = "名称", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    ServerResponse<List<Map<String,Object>>> getEdit(String name){
        return iLayerEditService.getEdit(name);
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "2.添加新数据", notes = "根据：<br/>name(名称)、<br/>geom(WKT类型的Geometry)，<br/>添加新数据.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "geom", value = "WKT类型的Geometry", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Integer> addEdit(String name, String geom) {
        return iLayerEditService.addEdit(name, geom);
    }

    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "3.更新数据", notes = "根据：<br/>id(唯一id)、<br/>name(名称)、<br/>geom(WKT类型的Geometry)，<br/>更新数据.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataTypeClass = int.class),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "geom", value = "WKT类型的Geometry", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Integer> updateEdit(int id, String name, String geom) {
        return iLayerEditService.updateEdit(id, name, geom);
    }
}
