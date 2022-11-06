package com.example.webgistest.service;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.geoserver.ImprovePostGISDatastore;
import com.example.webgistest.pojo.PgTableInfo;

import java.util.List;

public interface IPostGISService {

    /**
     * 连接PostGIS数据库并获取表名
     *
     * @param postgis postgis连接配置
     * @return 是否创建成功
     */
    ServerResponse<List<PgTableInfo>> getTableInfo(ImprovePostGISDatastore postgis);

}
