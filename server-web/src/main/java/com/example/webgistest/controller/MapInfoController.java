package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.MapInfo;
import com.example.webgistest.service.IMapInfoService;
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

import java.util.ArrayList;
import java.util.Map;

/**
 * 获取地图服务器相关信息
 */
@ApiSort(value = 2)
@Api(tags = "2.地图服务相关信息接口")
@Controller
@RequestMapping("mapinfo")
public class MapInfoController {

    @Autowired
    IMapInfoService iMapInfoService;

    /**
     * 获取所有工作空间
     *
     * @return 所有工作空间名称
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "1.获取所有工作空间", notes = "获取所有工作空间.")
    @RequestMapping(value = "/workspacenames", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ArrayList<String>> getWorkspaceNames() {
        return iMapInfoService.getWorkspaceNames();
    }

    /**
     * 获取所有数据存储
     *
     * @param workspaceName 工作空间名称
     * @return 数据存储名称
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "2.获取所有数据存储", notes = "根据：<br/>工作空间名称(workspaceName)，<br/>获取所有数据存储.")
    @ApiImplicitParam(name = "workspaceName", value = "工作空间名称", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/datastores", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ArrayList<String>> getDatastores(String workspaceName) {
        return iMapInfoService.getDatastores(workspaceName);
    }

    /**
     * 获取图层名称
     *
     * @return 图层名称
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "3.获取所有图层名称和标题", notes = "获取所有图层名称和标题.")
    @RequestMapping(value = "/layers", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ArrayList<Map<String, String>>> getLayers() {
        return iMapInfoService.getLayers();
    }

    /**
     * 获取图层信息
     *
     * @param layerName 图层名称
     * @return 图层信息
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "4.获取指定图层信息", notes = "根据：<br/>图层名(layerName)，<br/>获取图层信息.")
    @ApiImplicitParam(name = "layerName", value = "图层名称，例如tiger:poi", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/layer", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<MapInfo> getLayer(String layerName) {
        return iMapInfoService.getLayer(layerName);
    }

    /**
     * 获取样式名称
     *
     * @return 样式名称
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "5.获取所有样式名称", notes = "获取所有样式名称.")
    @RequestMapping(value = "/styles/1", method = RequestMethod.GET)
    @ResponseBody
    ServerResponse<ArrayList<String>> getStyles() {
        return iMapInfoService.getStyles();
    }

    /**
     * 获取样式名称
     *
     * @return 样式名称
     */
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "6.指定工作空间获取所有样式名称", notes = "根据：<br/>工作空间名称(workspaceName)，<br/>获取所有样式名称.")
    @ApiImplicitParam(name = "workspaceName", value = "工作空间名称", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/styles/2", method = RequestMethod.GET)
    @ResponseBody
    ServerResponse<ArrayList<String>> getStyles(String workspaceName) {
        return iMapInfoService.getStyles(workspaceName);
    }

    /**
     * 获取SLD 通过指定样式名
     *
     * @param styleName style 服务名称
     * @return 是否包含style服务
     * @ StyleServiceNotFoundException 样式不存在
     */
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "7.获取指定样式的SLD", notes = "根据：<br/>样式名(styleName)，<br/>获取SLD.")
    @ApiImplicitParam(name = "styleName", value = "样式名称，例如tiger:poi", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/sld", method = RequestMethod.GET)
    @ResponseBody
    ServerResponse<String> getSLD(String styleName) {
        return iMapInfoService.getSLD(styleName);
    }

}
