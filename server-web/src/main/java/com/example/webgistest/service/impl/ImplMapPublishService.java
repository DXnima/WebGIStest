package com.example.webgistest.service.impl;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.constant.RedisKey;
import com.example.webgistest.geoserver.GeoServerManager;
import com.example.webgistest.geoserver.ImprovePostGISDatastore;
import com.example.webgistest.service.IMapPublishService;
import com.example.webgistest.style.stylevariable.GeoType;
import com.example.webgistest.style.utils.StyleUtil;
import com.example.webgistest.utils.RedisUtil;
import org.geotools.styling.Style;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Map;

@Service
public class ImplMapPublishService implements IMapPublishService {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 创建工作空间
     *
     * @param workspaceName 工作空间名称
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> createWorkspace(String workspaceName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean create = manager.createWorkspace(workspaceName);
                if (create) {
                    return ServerResponse.createBySuccess("创建成功!", workspaceName);
                } else {
                    return ServerResponse.createByErrorMessage("创建失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 删除工作空间
     *
     * @param workspaceName 工作空间名称
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> removeWorkspace(String workspaceName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean remove = manager.removeWorkspace(workspaceName);
                if (remove) {
                    return ServerResponse.createBySuccess("删除成功!", workspaceName);
                } else {
                    return ServerResponse.createByErrorMessage("删除失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 创建样式
     *
     * @param sldBody   SLD XML
     * @param styleName 样式名称
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> createStyle(String sldBody, String styleName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean create = manager.createStyle(sldBody, styleName);
                if (create) {
                    return ServerResponse.createBySuccess("创建成功!", styleName);
                } else {
                    return ServerResponse.createByErrorMessage("创建失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 创建默认样式
     *
     * @param type      类型：point、line、polo
     * @param styleName 样式名称
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> createDefaultStyle(String styleName, String type) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
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
            try {
                String sldBody = StyleUtil.styleToString(style);
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean create = manager.createStyle(sldBody, styleName);
                if (create) {
                    return ServerResponse.createBySuccess("默认样式创建成功!", styleName);
                } else {
                    return ServerResponse.createByErrorMessage("默认样式创建失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 修改样式
     *
     * @param sldBody   SLD XML
     * @param styleName 样式名称
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> updateStyle(String sldBody, String styleName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean update = manager.updateStyle(sldBody, styleName);
                if (update) {
                    return ServerResponse.createBySuccess("修改成功!", styleName);
                } else {
                    return ServerResponse.createByErrorMessage("修改失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 删除样式
     *
     * @param styleName 样式名称
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> removeStyle(String styleName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean remove = manager.removeStyle(styleName);
                if (remove) {
                    return ServerResponse.createBySuccess("删除成功!", styleName);
                } else {
                    return ServerResponse.createByErrorMessage("删除失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 创建样式指定工作空间
     *
     * @param workspaceName 工作空间名称
     * @param sldBody       SLD XML
     * @param styleName     样式名称
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> createStyle(String workspaceName, String sldBody, String styleName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean create = manager.createStyleToWorkspace(workspaceName, sldBody, styleName);
                if (create) {
                    return ServerResponse.createBySuccess("创建成功!", workspaceName + ":" + styleName);
                } else {
                    return ServerResponse.createByErrorMessage("创建失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 创建默认样式
     *
     * @param workspaceName 工作空间名称
     * @param type          类型：point、line、polygon
     * @param styleName     样式名称
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> createDefaultStyle(String workspaceName, String styleName, String type) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
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
            try {
                String sldBody = StyleUtil.styleToString(style);
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean create = manager.createStyleToWorkspace(workspaceName, sldBody, styleName);
                if (create) {
                    return ServerResponse.createBySuccess("默认样式创建成功!", workspaceName + ":" + styleName);
                } else {
                    return ServerResponse.createByErrorMessage("默认样式创建失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage("默认样式创建失败！" + e.getMessage());
            }
        }
    }

    /**
     * 创建shp图层
     *
     * @param workspaceName 工作空间名称
     * @param shpFile       shp文件
     * @param crsCode       坐标系统代码
     * @param type          类型：POINT(点)、LINE(线)、POLYGON(面)
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> createShpLayerAndStyle(String workspaceName, String shpFile, int crsCode, String type) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else if (shpFile == null || shpFile.equals("")) {
            return ServerResponse.createByErrorCodeMessage(404, "shp文件为空！");
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean create = manager.createShpLayerAndStyle(workspaceName, new File(shpFile), crsCode, type);
                if (create) {
                    File file = new File(shpFile);
                    //    获取shp 名称
                    String shpFileName = file.getName();
                    //    获取shp 文件名切割数组
                    String split = shpFileName.split("\\.")[0];
                    file.delete();
                    return ServerResponse.createBySuccess("创建成功!", workspaceName + ":" + split);
                } else {
                    return ServerResponse.createByErrorMessage("创建失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 创建shp图层带样式
     *
     * @param workspaceName 工作空间名称
     * @param shpFile       shp文件
     * @param crsCode       坐标系统代码
     * @param styleName     样式名
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> createShpLayer(String workspaceName, String shpFile, int crsCode, String styleName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else if (shpFile == null || shpFile.equals("")) {
            return ServerResponse.createByErrorCodeMessage(404, "shp文件为空！");
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean create = manager.createShpLayer(workspaceName, new File(shpFile), crsCode, styleName);
                if (create) {
                    File file = new File(shpFile);
                    //    获取shp 名称
                    String shpFileName = file.getName();
                    //    获取shp 文件名切割数组
                    String split = shpFileName.split("\\.")[0];
                    file.delete();
                    return ServerResponse.createBySuccess("创建成功!", workspaceName + ":" + split);
                } else {
                    return ServerResponse.createByErrorMessage("创建失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 创建PostGIS图层
     *
     * @param postGIS       postGIS连接信息
     * @param workspaceName 作空间名称
     * @param tableName     表名
     * @param crsCode       坐标系统代码
     * @param type          类型：POINT(点)、LINE(线)、POLYGON(面)
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> createPostGISLayerAndStyle(ImprovePostGISDatastore postGIS, String workspaceName, String tableName, int crsCode, String type) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean create = manager.createPostGISLayerAndStyle(postGIS.builder(), workspaceName, tableName, crsCode, type);
                if (create) {
                    return ServerResponse.createBySuccess("创建成功!", workspaceName + ":" + tableName);
                } else {
                    return ServerResponse.createByErrorMessage("创建失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

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
    @Override
    public ServerResponse<String> createPostGISLayer(ImprovePostGISDatastore postGIS, String workspaceName, String tableName, int crsCode, String styleName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean create = manager.createPostGISLayer(postGIS.builder(), workspaceName, tableName, crsCode, styleName);
                if (create) {
                    return ServerResponse.createBySuccess("创建成功!", workspaceName + ":" + tableName);
                } else {
                    return ServerResponse.createByErrorMessage("创建失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 更新图层信息
     *
     * @param layerName 图层名，top:port1
     * @param title     图层标题
     * @param abstracts 图层描述
     * @return 图层是否修改成功
     */
    @Override
    public ServerResponse<String> updateLayer(String layerName, String title, String abstracts) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean status = layerName.contains(":");
                String workspaceName = null;
                if (status) {
                    workspaceName = layerName.split(":")[0];
                    layerName = layerName.split(":")[1];
                }
                if(manager.updateLayer(workspaceName, layerName, title, abstracts))
                    return ServerResponse.createBySuccess("图层信息修改成功!", workspaceName + ":" + layerName);
                else
                    return ServerResponse.createByErrorMessage("图层信息修改失败!");
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 修改矢量数据源编码格式
     *
     * @param workspaceName 要修改的矢量数据源所在的工作空间名称
     * @param dataStoreName 要修改的矢量数据源名称
     * @param encoding      编码格式
     * @return 是否删除成功
     * @ 数据源不存在
     * @ 工作空间不存在
     */
    @Override
    public ServerResponse<String> updateDataStore(String workspaceName, String dataStoreName, String encoding) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean remove = manager.updateDataStore(workspaceName, dataStoreName, encoding);
                if (remove) {
                    return ServerResponse.createBySuccess("删除成功!", workspaceName + ":" + dataStoreName);
                } else {
                    return ServerResponse.createByErrorMessage("删除失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 删除指定数据存储
     *
     * @param workspaceName 工作空间名称
     * @param dataStoreName 数据存储名称
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> removeDataStore(String workspaceName, String dataStoreName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                boolean remove = manager.removeDataStore(workspaceName, dataStoreName);
                if (remove) {
                    return ServerResponse.createBySuccess("删除成功!", workspaceName + ":" + dataStoreName);
                } else {
                    return ServerResponse.createByErrorMessage("删除失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * 删除指定图层
     *
     * @param layerName 图层名称
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<String> removeLayer(String layerName) {
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(RedisKey.GEOSERVER_CONFIG);
        if (!hasKey) {
            return ServerResponse.createByConfig();
        } else {
            try {
                Map<Object, Object> config = redisUtil.hmget(RedisKey.GEOSERVER_CONFIG);
                GeoServerManager manager = new GeoServerManager(config);
                String[] sq = layerName.split(":");
                boolean remove = manager.removeLayer(sq[0], sq[1]);
                if (remove) {
                    return ServerResponse.createBySuccess("删除成功!", layerName);
                } else {
                    return ServerResponse.createByErrorMessage("删除失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
    }

}
