package com.example.webgistest.service;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.geoserver.ImprovePostGISDatastore;

public interface IMapPublishService {

    /**
     * 创建工作空间
     *
     * @param workspaceName 工作空间名称
     * @return 是否创建成功
     */
    ServerResponse<String> createWorkspace(String workspaceName);

    /**
     * 删除工作空间
     *
     * @param workspaceName 工作空间名称
     * @return 是否创建成功
     */
    ServerResponse<String> removeWorkspace(String workspaceName);

    /**
     * 创建样式
     *
     * @param sldBody   SLD XML
     * @param styleName 样式名称
     * @return 是否创建成功
     */
    ServerResponse<String> createStyle(String sldBody, String styleName);

    /**
     * 创建默认样式
     *
     * @param type      类型：point、line、polygon
     * @param styleName 样式名称
     * @return 是否创建成功
     */
    ServerResponse<String> createDefaultStyle(String styleName, String type);


    /**
     * 修改样式指定工作空间
     *
     * @param sldBody   SLD XML
     * @param styleName 样式名称
     * @return 是否创建成功
     */
    ServerResponse<String> updateStyle(String sldBody, String styleName);

    /**
     * 删除样式
     *
     * @param styleName 样式名称
     * @return 是否创建成功
     */
    ServerResponse<String> removeStyle(String styleName);

    /**
     * 创建样式指定工作空间
     *
     * @param workspaceName 工作空间名称
     * @param sldBody       SLD XML
     * @param styleName     样式名称
     * @return 是否创建成功
     */
    ServerResponse<String> createStyle(String workspaceName, String sldBody, String styleName);

    /**
     * 创建默认样式
     *
     * @param workspaceName 工作空间名称
     * @param type          类型：point、line、polygon
     * @param styleName     样式名称
     * @return 是否创建成功
     */
    ServerResponse<String> createDefaultStyle(String workspaceName, String styleName, String type);

    /**
     * 创建shp图层
     *
     * @param workspaceName 工作空间名称
     * @param shpFile       shp文件
     * @param crsCode       坐标系统代码
     * @param type          类型：POINT(点)、LINE(线)、POLYGON(面)
     * @return 是否创建成功
     */
    ServerResponse<String> createShpLayerAndStyle(String workspaceName, String shpFile, int crsCode, String type);

    /**
     * 创建shp图层带样式
     *
     * @param workspaceName 工作空间名称
     * @param shpFile       shp文件
     * @param crsCode       坐标系统代码
     * @param styleName     样式名
     * @return 是否创建成功
     */
    ServerResponse<String> createShpLayer(String workspaceName, String shpFile, int crsCode, String styleName);

    /**
     * 创建PostGIS图层
     *
     * @param postGIS       postGIS连接信息
     * @param workspaceName 工作空间名称
     * @param tableName     表名
     * @param crsCode       坐标系统代码
     * @param type          类型：POINT(点)、LINE(线)、POLYGON(面)
     * @return 是否创建成功
     */
    ServerResponse<String> createPostGISLayerAndStyle(ImprovePostGISDatastore postGIS, String workspaceName, String tableName, int crsCode, String type);

    /**
     * 创建PostGIS图层带样式
     *
     * @param postGIS       postGIS连接信息
     * @param workspaceName 作空间名称
     * @param tableName     表名
     * @param crsCode       坐标系统代码
     * @param styleName     样式名
     * @return 是否创建成功
     */
    ServerResponse<String> createPostGISLayer(ImprovePostGISDatastore postGIS, String workspaceName, String tableName, int crsCode, String styleName);

    /**
     * 更新图层信息
     *
     * @param layerName 图层名，top:port1
     * @param title     图层标题
     * @param abstracts 图层描述
     * @return 图层是否修改成功
     */
    ServerResponse<String> updateLayer(String layerName, String title, String abstracts);


    /**
     * 修改矢量数据源编码格式
     *
     * @param workspaceName 要修改的矢量数据源所在的工作空间名称
     * @param dataStoreName 要修改的矢量数据源名称
     * @param encoding      编码格式
     * @return 是否删除成功
     */
    ServerResponse<String> updateDataStore(String workspaceName, String dataStoreName, String encoding);

    /**
     * 删除指定数据存储
     *
     * @param workspaceName 工作空间名称
     * @param dataStoreName 数据存储名称
     * @return 是否创建成功
     */
    ServerResponse<String> removeDataStore(String workspaceName, String dataStoreName);

    /**
     * 删除指定图层
     *
     * @param layerName 图层名称
     * @return 是否创建成功
     */
    ServerResponse<String> removeLayer(String layerName);

}
