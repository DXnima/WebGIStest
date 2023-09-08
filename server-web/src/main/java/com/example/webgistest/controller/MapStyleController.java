package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.MapStyle;
import com.example.webgistest.service.IMapStyleService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地图样式
 */
@ApiSort(value = 4)
@Api(tags = "4.地图样式接口")
@Controller
@RequestMapping("style")
public class MapStyleController {

    @Autowired
    IMapStyleService iMapStyleService;

    /**
     * 样式修改
     *
     * @param mapStyle 样式参数对象
     * @param styleName  样式名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "1.指定样式名称修改", notes = "根据：<br/>样式参数对象(styleValue)、<br/>样式名称,例如tiger:poi(styleName)，<br/>进行点样式修改.")
    @ApiImplicitParam(name = "styleName", value = "样式名称，例如tiger:poi", required = true, dataTypeClass = String.class, paramType = "path")
    @RequestMapping(value = "/1/{styleName}", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> changeStyleByStyleName(@RequestBody List<MapStyle> mapStyle, @PathVariable("styleName") String styleName) {
        return iMapStyleService.changeStyleByStyleName(mapStyle, styleName);
    }

    /**
     * 修改指定图层的样式
     *
     * @param mapStyle 样式参数对象
     * @param layerName  图层名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "2.修改指定图层的样式", notes = "根据：<br/>样式参数对象(styleValue)、<br/>图层名称(layerName)，<br/>进行点样式修改.")
    @ApiImplicitParam(name = "layerName", value = "图层名称，例如tiger:poi", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/2/{layerName}", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<String> changeStyleByLayerName(@RequestBody List<MapStyle> mapStyle, @PathVariable("layerName") String layerName) {
        return iMapStyleService.changeStyleByLayerName(mapStyle, layerName);
    }

    /**
     * 根据样式获取样式参数
     *
     * @param styleName 样式名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "3.根据样式名称获取样式参数", notes = "根据：<br/>样式名称(styleName)，<br/>获取样式参数.")
    @ApiImplicitParam(name = "styleName", value = "样式名称，例如tiger:poi", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/getvalue/1", method = RequestMethod.GET)
    @ResponseBody
    ServerResponse<List<MapStyle>> getStyleValueByStyleName(String styleName) {
        return iMapStyleService.getStyleValueByStyleName(styleName);
    }

    /**
     * 根据图层获取样式参数
     *
     * @param layerName 图层名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "4.根据图层获取样式参数", notes = "根据：<br/>图层名称(layerName)，<br/>获取样式参数.")
    @ApiImplicitParam(name = "layerName", value = "图层名称，例如tiger:poi", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/getvalue/2", method = RequestMethod.GET)
    @ResponseBody
    ServerResponse<List<MapStyle>> getStyleValueByLayerName(String layerName) {
        return iMapStyleService.getStyleValueByLayerName(layerName);
    }

    /**
     * 更改为默认样式
     *
     * @param styleName 样式名称
     * @param type      类型点、线、面
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "5.指定样式更改为默认样式", notes = "根据：<br/>样式名称(styleName)，<br/>获取样式参数.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "styleName", value = "样式名称，例如tiger:poi", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "type", value = "类型(大小写都可)：point、line、polygon", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/default", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<String> changeDefaultStyle(String styleName, String type) {
        return iMapStyleService.changeDefaultStyle(styleName, type);
    }

}
