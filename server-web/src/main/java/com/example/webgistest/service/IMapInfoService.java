package com.example.webgistest.service;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.MapInfo;

import java.util.ArrayList;
import java.util.Map;

public interface IMapInfoService {

    /**
     * 获取所有工作空间
     *
     * @return 所有工作空间名称
     */
    ServerResponse<ArrayList<String>> getWorkspaceNames();

    /**
     * 获取所有数据存储
     *
     * @param workspaceName 工作空间名称
     * @return 数据存储名称
     */
    ServerResponse<ArrayList<String>> getDatastores(String workspaceName);

    /**
     * 获取图层名称
     *
     * @return 图层名称
     */
    ServerResponse<ArrayList<Map<String, String>>> getLayers();

    /**
     * 获取图层信息
     *
     * @param layerName 图层名称
     * @return 图层信息
     */
    ServerResponse<MapInfo> getLayer(String layerName);

    /**
     * 获取样式名称
     *
     * @return 样式名称
     */
    ServerResponse<ArrayList<String>> getStyles();

    /**
     * 获取样式名称
     *
     * @return 样式名称
     */
    ServerResponse<ArrayList<String>> getStyles(String workspaceName);

    /**
     * 获取SLD 通过指定样式名
     *
     * @param styleName style 服务名称
     * @return 是否包含style服务
     */
    ServerResponse<String> getSLD(String styleName);

}
