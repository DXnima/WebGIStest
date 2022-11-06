package com.example.webgistest.service;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.MapStyle;

import java.util.List;

public interface IMapStyleService {

    /**
     * 样式修改
     *
     * @param mapStyle 样式参数对象
     * @param styleName  样式名称
     * @return 是否创建成功
     */
    ServerResponse<String> changeStyleByStyleName(List<MapStyle> mapStyle, String styleName);

    /**
     * 样式修改
     *
     * @param mapStyle 样式参数对象
     * @param layerName  图层名称
     * @return 是否创建成功
     */
    ServerResponse<String> changeStyleByLayerName(List<MapStyle> mapStyle, String layerName);

    /**
     * 获取样式参数
     *
     * @param styleName 样式名称
     * @return 是否创建成功changeStyleByStyleName
     */
    ServerResponse<List<MapStyle>> getStyleValueByStyleName(String styleName);

    /**
     * 获取样式参数
     *
     * @param layerName 图层名称
     * @return 是否创建成功changeStyleByStyleName
     */
    ServerResponse<List<MapStyle>> getStyleValueByLayerName(String layerName);

    /**
     * 更改为默认样式
     *
     * @param styleName 样式名称
     * @param type 类型点、线、面
     * @return 是否创建成功
     */
    ServerResponse<String> changeDefaultStyle(String styleName,String type);


}
