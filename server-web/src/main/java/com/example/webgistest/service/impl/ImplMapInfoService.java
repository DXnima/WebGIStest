package com.example.webgistest.service.impl;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.constant.RedisKey;
import com.example.webgistest.geoserver.GeoServerManager;
import com.example.webgistest.geoserver.GeoServerReader;
import com.example.webgistest.pojo.MapInfo;
import com.example.webgistest.service.IMapInfoService;
import com.example.webgistest.utils.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;

@Service
public class ImplMapInfoService implements IMapInfoService {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取所有工作空间
     *
     * @return 所有工作空间名称
     */
    @Override
    public ServerResponse<ArrayList<String>> getWorkspaceNames() {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerReader reader = new GeoServerReader(config);
                return ServerResponse.createBySuccess("工作空间获取成功！", reader.getWorkspaces());
            } catch (MalformedURLException e) {
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 获取所有数据存储
     *
     * @param workspaceName 工作空间名称
     * @return 数据存储名称
     */
    @Override
    public ServerResponse<ArrayList<String>> getDatastores(String workspaceName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerReader reader = new GeoServerReader(config);
                return ServerResponse.createBySuccess("数据存储获取成功！", reader.getDataStores(workspaceName));
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 获取图层名称
     *
     * @return 图层名称
     */
    @Override
    public ServerResponse<ArrayList<Map<String, String>>> getLayers() {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerReader reader = new GeoServerReader(config);
                return ServerResponse.createBySuccess("图层获取成功！", reader.getLayersList());
            } catch (Exception e) {
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 获取图层信息
     *
     * @param layerName 图层名称
     * @return 图层信息
     */
    @Override
    public ServerResponse<MapInfo> getLayer(String layerName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerReader reader = new GeoServerReader(config);
                String workspaceName = layerName.split(":")[0];
                String layerN = layerName.split(":")[1];
                return ServerResponse.createBySuccess("图层信息获取成功！", reader.getLayer(workspaceName, layerN, config.get("restURL").toString()));
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 获取样式名称
     *
     * @return 样式名称
     */
    @Override
    public ServerResponse<ArrayList<String>> getStyles() {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerReader reader = new GeoServerReader(config);
                return ServerResponse.createBySuccess("样式获取成功！", reader.getStyles());
            } catch (MalformedURLException e) {
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 获取样式名称
     *
     * @return 样式名称
     */
    @Override
    public ServerResponse<ArrayList<String>> getStyles(String workspaceName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerReader reader = new GeoServerReader(config);
                return ServerResponse.createBySuccess("样式获取成功！", reader.getStyles(workspaceName));
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 获取SLD 通过指定样式名
     *
     * @param styleName style 服务名称
     * @return 是否包含style服务
     */
    public ServerResponse<String> getSLD(String styleName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                return ServerResponse.createBySuccess("SLD获取成功！", manager.getSLD(styleName));
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

}
