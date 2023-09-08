package com.example.webgistest.utils;

import com.alibaba.druid.util.JdbcUtils;
import com.example.webgistest.exception.ErrorException;
import com.example.webgistest.pojo.PgTableInfo;
import com.example.webgistest.style.stylevariable.GeoType;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlUtil {

    public static List<PgTableInfo> getTableInfo(DataSource dataSource) throws ErrorException {
        List<PgTableInfo> pgTableInfoList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            List<Map<String, Object>> tableNames = getGeomTable(dataSource);
            assert tableNames != null;
            for (Map<String,Object> item : tableNames){
                if (item.get("name").toString().contains("planet")) continue;
                PgTableInfo pgTableInfo = new PgTableInfo();
                String sql = String.format("SELECT reltuples::bigint AS number FROM pg_class WHERE  oid = 'public.\"%s\"'::regclass", item.get("name"));
                String number = new JdbcTemplate(dataSource).queryForMap(sql).get("number").toString();
                if (number.equals("0")) continue;
                sql = String.format("select geometrytype(%s) as type from \"%s\" limit 1", item.get("column"), item.get("name"));
                String type = new JdbcTemplate(dataSource).queryForMap(sql).get("type").toString();
                pgTableInfo.setName(item.get("name").toString());
                pgTableInfo.setType(GeoType.getTypeGeom(type));
                pgTableInfo.setGeom(item.get("column").toString());
                pgTableInfo.setNumber(number);
                pgTableInfoList.add(pgTableInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ErrorException("数据库表信息获取失败！");
        } finally {
            JdbcUtils.close(connection);
        }
        return pgTableInfoList;
    }

    /**
     * 获取空间数据库的表名
     *
     * @param dataSource
     * @return void
     * @author wnm
     * @date 2020/12/30
     */
    public static List<Map<String, Object>> getGeomTable(DataSource dataSource) {
        try {

            // 使用jdbc原生方法获取
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            return jdbcTemplate.queryForList(
                    "SELECT table_name AS NAME,column_name AS COLUMN " +
                    "FROM information_schema.COLUMNS " +
                    "WHERE table_schema = 'public' AND udt_name = 'geometry' "
            );
        } catch (Throwable e) {
            // 获取表失败！
            return null;
        }
    }

}
