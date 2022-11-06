package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.PgEdit;
import com.example.webgistest.pojo.ShapeFileConfig;
import com.example.webgistest.service.IMapPublishService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 地图服务操作
 */
@ApiSort(value = 3)
@Api(tags = "3.地图服务操作")
@Controller
@RequestMapping("publish")
public class MapPublishController {

    @Autowired
    IMapPublishService iMapPublishService;

    /**
     * 创建工作空间
     *
     * @param workspaceName 工作空间名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "1.创建工作空间", notes = "根据：<br/>工作空间名称(workspaceName)，<br/>创建工作空间.")
    @ApiImplicitParam(name = "workspaceName", value = "工作空间名称", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/workspace", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> createWorkspace(String workspaceName) {
        return iMapPublishService.createWorkspace(workspaceName);
    }

    /**
     * 删除工作空间
     *
     * @param workspaceName 工作空间名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "2.删除工作空间", notes = "根据：<br/>工作空间名称(workspaceName)，<br/>删除工作空间.")
    @ApiImplicitParam(name = "workspaceName", value = "工作空间名称", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/workspace", method = RequestMethod.DELETE)
    @ResponseBody
    public ServerResponse<String> removeWorkspace(String workspaceName) {
        return iMapPublishService.removeWorkspace(workspaceName);
    }

    /**
     * 创建样式
     *
     * @param sldBody   SLD XML
     * @param styleName 样式名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "3.发布样式", notes = "根据：<br/>SLD(sldBody)、<br/>样式名称(styleName)，<br/>发布样式.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sldBody", value = "SLD", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "styleName", value = "样式名称", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/style/1", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> createStyle(String sldBody, String styleName) {
        return iMapPublishService.createStyle(sldBody, styleName);
    }

    /**
     * 创建指定类型的默认样式
     *
     * @param type      类型：point、line、polygon
     * @param styleName 样式名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "4.发布指定类型的默认样式", notes = "根据：<br/>类型：point、line、polygon(type)、<br/>样式名称(styleName)，<br/>发布样式.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "styleName", value = "样式名称", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "type", value = "类型(大小写都可)：point、line、polygon", required = true, dataTypeClass = String.class),
    })
    @RequestMapping(value = "/defaultstyle/1", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> createDefaultStyle(String styleName, String type) {
        return iMapPublishService.createDefaultStyle(styleName, type);
    }

    /**
     * 创建样式指定工作空间
     *
     * @param workspaceName 工作空间名称
     * @param sldBody       SLD XML
     * @param styleName     样式名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "5.指定工作空间创建样式", notes = "根据：<br/>工作空间(workspaceName)、<br/>SLD(sldBody)、<br/>样式名称(styleName)，<br/>发布样式.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workspaceName", value = "工作空间名称", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "sldBody", value = "SLD", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "styleName", value = "样式名称", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/style/2", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> createStyle(String workspaceName, String sldBody, String styleName) {
        return iMapPublishService.createStyle(workspaceName, sldBody, styleName);
    }

    /**
     * 创建默认样式
     *
     * @param workspaceName 工作空间名称
     * @param type          类型：point、line、polygon
     * @param styleName     样式名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "6.指定工作空间创建默认样式", notes = "根据：<br/>工作空间(workspaceName)、<br/>类型：point、line、polygon(type)、<br/>样式名称(styleName)，<br/>发布样式.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workspaceName", value = "工作空间名称", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "styleName", value = "样式名称", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "type", value = "类型(大小写都可)：point、line、polygon", required = true, dataTypeClass = String.class),
    })
    @RequestMapping(value = "/defaultstyle/2", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> createDefaultStyle(String workspaceName, String styleName, String type) {
        return iMapPublishService.createDefaultStyle(workspaceName, styleName, type);
    }

    /**
     * 修改样式
     *
     * @param sldBody   SLD XML
     * @param styleName 样式名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "7.修改样式", notes = "根据：<br/>SLD(sldBody)、<br/>样式名称(styleName)，<br/>修改样式.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sldBody", value = "SLD", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "styleName", value = "样式名称，例如tiger:poi", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/style", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<String> updateStyle(String sldBody, String styleName) {
        return iMapPublishService.updateStyle(sldBody, styleName);
    }

    /**
     * 删除样式
     *
     * @param styleName 样式名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "8.删除样式", notes = "根据：<br/>样式名称(styleName)，<br/>删除样式.")
    @ApiImplicitParam(name = "styleName", value = "样式名称，例如tiger:poi", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/style", method = RequestMethod.DELETE)
    @ResponseBody
    public ServerResponse<String> removeStyle(String styleName) {
        return iMapPublishService.removeStyle(styleName);
    }

    /**
     * 发布shp图层，并自动创建样式
     *
     * @param shp shp图层发布配置
     * @param type 类型：POINT(点)、LINE(线)、POLYGON(面)
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "9.发布shp图层并自动创建样式", notes = "根据：<br/>工作空间(workspaceName)、<br/>文件(shpFile)、<br/>坐标系统代码(crsCode)、<br/>类型(type)，<br/>发布shp图层.")
    @ApiImplicitParam(name = "type", value = "要素类型，可选：POINT(点)、LINE(线)、POLYGON(面)", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/shplayer/{type}", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> createShpLayer1(@RequestBody ShapeFileConfig shp, @PathVariable("type") String type) {
        return iMapPublishService.createShpLayerAndStyle(shp.getWorkspaceName(), shp.getShpFile(), shp.getCrsCode(), type);
    }

    /**
     * 发布shp图层带样式
     *
     * @param shp shp图层发布配置
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "10.发布shp图层", notes = "根据：<br/>工作空间(workspaceName)、<br/>文件(shpFile)、<br/>坐标系统代码(crsCode)、<br/>样式名称,例如tiger:poi(styleName)，<br/>发布shp图层.")
    @RequestMapping(value = "/shplayer2", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> createShpLayer2(@RequestBody ShapeFileConfig shp) {
        return iMapPublishService.createShpLayer(shp.getWorkspaceName(), shp.getShpFile(), shp.getCrsCode(), shp.getStyleName());
    }

    /**
     * 发布PostGIS图层，并自动创建样式
     *
     * @param postgis postGIS连接信息
     * @param type          类型：POINT(点)、LINE(线)、POLYGON(面)
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 11)
    @ApiOperation(value = "11.发布PostGIS图层并自动创建样式", notes = "根据：<br/>postGIS连接信息(postGIS)、<br/>工作空间(workspaceName)、<br/>表名(tableName)、<br/>坐标系统代码(crsCode)、<br/>类型(类型)，<br/>发布PostGIS图层.")
    @ApiImplicitParam(name = "type", value = "要素类型，可选：POINT(点)、LINE(线)、POLYGON(面)", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/postgislayer/{type}", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> createPostGISLayer1(@RequestBody PgEdit postgis, @PathVariable("type") String type) {
        return iMapPublishService.createPostGISLayerAndStyle(postgis.getPostGIS(), postgis.getWorkspaceName(), postgis.getTableName(), postgis.getCrsCode(), type);
    }

    /**
     * 发布PostGIS图层带样式
     *
     * @param postgis postGIS连接信息
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 12)
    @ApiOperation(value = "12.发布PostGIS图层", notes = "根据：<br/>postGIS连接信息(postGIS)、<br/>工作空间(workspaceName)、<br/>表名(tableName)、<br/>坐标系统代码(crsCode)、<br/>样式名称,例如tiger:poi(styleName)，<br/>发布PostGIS图层.")
    @RequestMapping(value = "/postgislayer2", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> createPostGISLayer2(@RequestBody PgEdit postgis) {
        return iMapPublishService.createPostGISLayer(postgis.getPostGIS(), postgis.getWorkspaceName(), postgis.getTableName(), postgis.getCrsCode(), postgis.getStyleName());
    }

    /**
     * 修改矢量数据源编码格式
     *
     * @param workspaceName 要修改的矢量数据源所在的工作空间名称
     * @param dataStoreName 要修改的矢量数据源名称
     * @param encoding      编码格式
     * @return 是否修改成功
     * @ 数据源不存在
     * @ 工作空间不存在
     */
    @ApiOperationSupport(order = 13)
    @ApiOperation(value = "13.修改矢量数据存储编码格式", notes = "根据：<br/>工作空间名称(workspaceName)、<br/>数据存储名称(dataStoreName)、<br/>编码格式(encoding)，<br/>修改数据存储.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workspaceName", value = "工作空间名称", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "dataStoreName", value = "数据存储名称", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "encoding", value = "编码格式，例如GBK", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/datastore", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<String> updateDataStore(String workspaceName, String dataStoreName, String encoding) {
        return iMapPublishService.updateDataStore(workspaceName, dataStoreName, encoding);
    }

    /**
     * 删除指定数据存储
     *
     * @param workspaceName 工作空间名称
     * @param dataStoreName 数据存储名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 14)
    @ApiOperation(value = "14.指定工作空间删除指定数据存储", notes = "根据：<br/>工作空间名称(workspaceName)、<br/>数据存储名称(dataStoreName)，<br/>删除数据存储.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workspaceName", value = "工作空间名称", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "dataStoreName", value = "数据存储名称", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/datastore", method = RequestMethod.DELETE)
    @ResponseBody
    public ServerResponse<String> removeDataStore(String workspaceName, String dataStoreName) {
        return iMapPublishService.removeDataStore(workspaceName, dataStoreName);
    }

    /**
     * 更新图层信息
     *
     * @param layerName 图层名，top:port1
     * @param title     图层标题
     * @param abstracts 图层描述
     * @return 图层是否修改成功
     */
    @ApiOperationSupport(order = 15)
    @ApiOperation(value = "15.更新图层信息", notes = "根据：<br/>图层名(layerName)、<br/>图层标题(title)、<br/>图层描述信息(abstracts)，<br/>删除数据存储.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "layerName", value = "图层名称，例如tiger:poi", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "title", value = "图层标题", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "abstracts", value = "图层描述信息(备注)", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/layer", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<String> updateLayer(String layerName, String title, String abstracts) {
        return iMapPublishService.updateLayer(layerName, title, abstracts);
    }

    /**
     * 删除指定图层
     *
     * @param layerName 图层名称
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 16)
    @ApiOperation(value = "16.指定工作空间删除指定图层", notes = "根据：<br/>图层名称(layerName)，<br/>删除图层.")
    @ApiImplicitParam(name = "layerName", value = "图层名称，例如tiger:poi", required = true, dataTypeClass = String.class)
    @RequestMapping(value = "/layer", method = RequestMethod.DELETE)
    @ResponseBody
    public ServerResponse<String> removeLayer(String layerName) {
        return iMapPublishService.removeLayer(layerName);
    }

}
