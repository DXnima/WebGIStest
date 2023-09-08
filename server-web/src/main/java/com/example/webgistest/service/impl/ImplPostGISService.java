package com.example.webgistest.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.geoserver.ImprovePostGISDatastore;
import com.example.webgistest.pojo.PgTableInfo;
import com.example.webgistest.service.IPostGISService;
import com.example.webgistest.utils.SqlUtil;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.util.List;

@Service
public class ImplPostGISService implements IPostGISService {

    /**
     * 连接PostGIS数据库并获取表名
     *
     * @param postgis postgis连接配置
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<List<PgTableInfo>> getTableInfo(ImprovePostGISDatastore postgis) {
        DruidDataSource dataSource = new DruidDataSource();
        try {
            String url = String.format("jdbc:postgresql://%s:%s/%s", postgis.getHost(), postgis.getPort(), postgis.getDatabase());
            DriverManager.getConnection(url, postgis.getUser(), postgis.getPassword());
            dataSource.setUrl(url);
            dataSource.setUsername(postgis.getUser());
            dataSource.setPassword(postgis.getPassword());
            dataSource.setTestWhileIdle(false);
            dataSource.setFailFast(true);
            List<PgTableInfo> getTableNames = SqlUtil.getTableInfo(dataSource);
            return ServerResponse.createBySuccess("数据库连接成功！", getTableNames);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("PostGIS连接失败！");
        } finally {
            JdbcUtils.close(dataSource);
        }
    }

}
