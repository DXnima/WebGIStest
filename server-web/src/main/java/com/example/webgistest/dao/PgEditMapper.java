package com.example.webgistest.dao;

import java.util.List;
import java.util.Map;

public interface PgEditMapper {
    // 获取空间数据库的表名
    List<Map<String, Object>> getGeomTable();

    //获取指定表的总行数
    int  getTableNumber(String tableName);

    //获取指定表空间字段的类型
    String  getGeomType(String tableName, String geom);

    //获取指定表字段
    List<String>  getTableField(String tableName);

    //获取指定表格数据
    List<Map<String, Object>> getTableData(String tableName, String geom, int page, int pageSize);

    //查找指定表格数据
    List<Map<String, Object>> findTableData(String tableName, String geom, List<String> fields, String query, int page, int pageSize);

    //向指定表格插入数据
    boolean insertTableData(String tableName, String geom, Map<String, Object> newRow, List<String> fields);

    //向指定表格更新数据
    boolean updateTableData(String tableName, String geom, String oldGeom, Map<String, Object> newRow, List<String> fields);

    //向指定表格删除数据
    boolean deleteTableData(String tableName, String geom, String oldGeom);

}
