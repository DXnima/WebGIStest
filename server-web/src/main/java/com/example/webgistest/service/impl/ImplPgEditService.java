package com.example.webgistest.service.impl;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.dao.PgEditMapper;
import com.example.webgistest.pojo.PgFieldTableInfo;
import com.example.webgistest.pojo.PgRowInfo;
import com.example.webgistest.pojo.PgTableInfo;
import com.example.webgistest.service.IPgEditService;
import com.example.webgistest.style.stylevariable.GeoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImplPgEditService implements IPgEditService {

    @Autowired
    PgEditMapper pgEditMapper;

    /**
     * 连接PostGIS本地数据库并获取表名
     *
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<List<PgTableInfo>> getTableInfo() {
        List<PgTableInfo> pgTableInfoList = new ArrayList<>();
        List<Map<String, Object>> tableNames = pgEditMapper.getGeomTable();
        if (tableNames.size() == 0)
            return ServerResponse.createByErrorMessage("获取失败！");
        for (Map<String,Object> item : tableNames){
            if (item.get("name").toString().contains("planet")) continue;
            PgTableInfo pgTableInfo = new PgTableInfo();
            int number = pgEditMapper.getTableNumber(item.get("name").toString());
            if (number == 0) continue;
            String type = pgEditMapper.getGeomType(item.get("name").toString(), item.get("column").toString());
            pgTableInfo.setName(item.get("name").toString());
            pgTableInfo.setType(GeoType.getTypeGeom(type));
            pgTableInfo.setGeom(item.get("column").toString());
            pgTableInfo.setNumber(number + "");
            pgTableInfoList.add(pgTableInfo);
        }
        return ServerResponse.createBySuccess("获取成功！", pgTableInfoList);
    }

    /**
     * 连接PostGIS本地数据库并获取表名
     *
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<Map<String, Object>> getTableData(PgFieldTableInfo pgFieldTableInfo) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> listData = pgEditMapper.getTableData(pgFieldTableInfo.getTableName(), pgFieldTableInfo.getGeom(), pgFieldTableInfo.getPageNum(), pgFieldTableInfo.getPageSize());
        if (listData.size() == 0)
            return ServerResponse.createByErrorMessage("获取失败！");
        map.put("data", listData);
        List<String> fields = pgEditMapper.getTableField(pgFieldTableInfo.getTableName());
        if (fields.size() == 0)
            return ServerResponse.createByErrorMessage("获取失败！");
        map.put("fields", fields);
        return ServerResponse.createBySuccess("获取成功！", map);
    }

    /**
     * 连接PostGIS数据库并查询表数据
     *
     * @return 是否创建成功
     */
    @Override
    public ServerResponse<Map<String, Object>> findTableData(PgFieldTableInfo pgFieldTableInfo){
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> listData = pgEditMapper.findTableData(pgFieldTableInfo.getTableName(), pgFieldTableInfo.getGeom(), pgFieldTableInfo.getFields(), pgFieldTableInfo.getQuery(), pgFieldTableInfo.getPageNum(), pgFieldTableInfo.getPageSize());
        if (listData.size() == 0)
            return ServerResponse.createByErrorMessage("获取失败！");
        return ServerResponse.createBySuccess("获取成功！", map);
    }

    /**
     * 添加数据
     *
     * @return 是否成功
     */
    @Override
    public ServerResponse<String> addTableData(PgRowInfo pgRowInfo){
        boolean add = pgEditMapper.insertTableData(pgRowInfo.getTableName(), pgRowInfo.getGeom(), pgRowInfo.getNewRow(), pgRowInfo.getFields());
        if (add)
            return ServerResponse.createBySuccessMessage("数据添加成功！");
        else
            return ServerResponse.createByErrorMessage("数据添加失败！");
    }

    /**
     * 更新数据
     *
     * @return 是否成功
     */
    @Override
    public ServerResponse<String> updateTableData(PgRowInfo pgRowInfo){
        boolean update = pgEditMapper.updateTableData(pgRowInfo.getTableName(), pgRowInfo.getGeom(), pgRowInfo.getOldGeom(),pgRowInfo.getNewRow(), pgRowInfo.getFields());
        if (update)
            return ServerResponse.createBySuccessMessage("数据添加成功！");
        else
            return ServerResponse.createByErrorMessage("数据添加失败！");
    }

    /**
     * 删除数据
     *
     * @return 是否成功
     */
    @Override
    public ServerResponse<String> deleteTableData(PgRowInfo pgRowInfo){
        boolean del = pgEditMapper.deleteTableData(pgRowInfo.getTableName(), pgRowInfo.getGeom(), pgRowInfo.getOldGeom());
        if (del)
            return ServerResponse.createBySuccessMessage("数据添加成功！");
        else
            return ServerResponse.createByErrorMessage("数据添加失败！");
    }

}
