package com.example.webgistest.service.impl;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.constant.RedisKey;
import com.example.webgistest.geoserver.GeoServerManager;
import com.example.webgistest.geoserver.GeoServerReader;
import com.example.webgistest.pojo.MapInfo;
import com.example.webgistest.pojo.MapStyle;
import com.example.webgistest.service.IMapStyleService;
import com.example.webgistest.style.StyleDetail;
import com.example.webgistest.style.stylevariable.GeoType;
import com.example.webgistest.style.utils.StyleUtil;
import com.example.webgistest.utils.RedisUtil;
import org.geotools.styling.Style;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ImplMapStyleService implements IMapStyleService {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 样式修改
     *
     * @param mapStyle 样式参数对象
     * @param styleName  样式名称
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> changeStyleByStyleName(List<MapStyle> mapStyle, String styleName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {

            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);

                //修改样式
                String sld = manager.getSLD(styleName);
                StyleDetail styleDetail = new StyleDetail(sld);
                styleDetail.changeStyles(mapStyle);
                String sldBody = styleDetail.getSld();

                //更新样式
                manager.updateStyle(sldBody, styleName);
                return ServerResponse.createBySuccess("修改成功!", styleName);
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage("样式修改失败！" + e.getMessage());
            }
        }
    }

    /**
     * 样式修改
     *
     * @param mapStyle 样式参数对象
     * @param layerName  图层名称
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> changeStyleByLayerName(List<MapStyle> mapStyle, String layerName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerReader reader = new GeoServerReader(config);
                GeoServerManager manager = new GeoServerManager(config);
                boolean status = layerName.contains(":");
                String workspaceName = "", layerN = "";
                if (status) {
                    workspaceName = layerName.split(":")[0];
                    layerN = layerName.split(":")[1];
                } else {
                    return ServerResponse.createByErrorMessage("图层名称异常！");
                }
                MapInfo mapInfo = reader.getLayer(workspaceName, layerN, config.get("restURL").toString());
                String styleName = mapInfo.getStyle();

                //修改样式
                String sld = manager.getSLD(styleName);
                StyleDetail styleDetail = new StyleDetail(sld);
                styleDetail.changeStyles(mapStyle);
                String sldBody = styleDetail.getSld();

                //更新样式
                manager.updateStyle(sldBody, styleName);
                return ServerResponse.createBySuccess("修改成功!", styleName);
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage("样式修改失败！" + e.getMessage());
            }
        }
    }

    /**
     * 获取样式参数
     *
     * @param styleName 样式名称
     * @return 是否创建成功changeStyleByStyleName
     */
    @Override
    public ServerResponse<List<MapStyle>> getStyleValueByStyleName(String styleName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);

                //获取样式
                String sld = manager.getSLD(styleName);
                StyleDetail styleDetail = new StyleDetail(sld);
                return ServerResponse.createBySuccess("获取样式参数成功!", styleDetail.getStyleValue());
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage("获取样式参数失败！" + e.getMessage());
            }
        }
    }

    /**
     * 获取样式参数
     *
     * @param layerName 图层名称
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<List<MapStyle>> getStyleValueByLayerName(String layerName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerReader reader = new GeoServerReader(config);
                String workspaceName = "", layerN = "";
                boolean status = layerName.contains(":");
                if (status) {
                    workspaceName = layerName.split(":")[0];
                    layerN = layerName.split(":")[1];
                } else {
                    return ServerResponse.createByErrorMessage("图层名称异常！");
                }
                MapInfo mapInfo = reader.getLayer(workspaceName, layerN, config.get("restURL").toString());
                String styleName = mapInfo.getStyle();

                //获取样式
                GeoServerManager manager = new GeoServerManager(config);
                String sld = manager.getSLD(styleName);
                StyleDetail styleDetail = new StyleDetail(sld);


                return ServerResponse.createBySuccess("获取样式参数成功!", styleDetail.getStyleValue());
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage("获取样式参数失败！" + e.getMessage());
            }
        }
    }

    /**
     * 更改为默认样式
     *
     * @param styleName 样式名称
     * @param type      类型点、线、面
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> changeDefaultStyle(String styleName, String type) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Style style;
                switch (GeoType.getType(type)) {
                    case POINT:
                        style = StyleUtil.createDefaultPointStyle();
                        break;
                    case LINE:
                        style = StyleUtil.createDefaultLineStyle();
                        break;
                    case POLYGON:
                        style = StyleUtil.createDefaultPolygonStyle();
                        break;
                    default:
                        return ServerResponse.createByErrorMessage("类型不对，默认样式创建失败！");
                }
                String sldBody = StyleUtil.styleToString(style);
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean update = manager.updateStyle(sldBody, styleName);
                if (update) {
                    return ServerResponse.createBySuccess("默认样式修改成功!", styleName);
                } else {
                    return ServerResponse.createByErrorMessage("默认样式修改失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage("默认样式修改失败！" + e.getMessage());
            }
        }
    }

}
