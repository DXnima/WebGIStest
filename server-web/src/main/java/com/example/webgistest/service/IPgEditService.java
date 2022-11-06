package com.example.webgistest.service;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.PgFieldTableInfo;
import com.example.webgistest.pojo.PgRowInfo;
import com.example.webgistest.pojo.PgTableInfo;

import java.util.List;
import java.util.Map;

public interface IPgEditService {

    /**
     * 连接PostGIS本地数据库并获取表名
     *
     * @return 是否创建成功
     */
    ServerResponse<List<PgTableInfo>> getTableInfo();

    /**
     * 连接PostGIS数据库并获取表数据
     *
     * @return 是否成功
     */
    ServerResponse<Map<String, Object>> getTableData(PgFieldTableInfo pgFieldTableInfo);

    /**
     * 连接PostGIS数据库并查询表数据
     *
     * @return 是否成功
     */
    ServerResponse<Map<String, Object>> findTableData(PgFieldTableInfo pgFieldTableInfo);

    /**
     * 添加数据
     *
     * @return 是否成功
     */
    ServerResponse<String> addTableData(PgRowInfo pgRowInfo);

    /**
     * 更新数据
     *
     * @return 是否成功
     */
    ServerResponse<String> updateTableData(PgRowInfo pgRowInfo);

    /**
     * 删除数据
     *
     * @return 是否成功
     */
    ServerResponse<String> deleteTableData(PgRowInfo pgRowInfo);

}
