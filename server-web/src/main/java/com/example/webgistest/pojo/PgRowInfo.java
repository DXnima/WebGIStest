package com.example.webgistest.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

@ApiModel(value = "数据库行信息类")
public class PgRowInfo {

    @ApiModelProperty(value = "表名")
    private String tableName;
    @ApiModelProperty(value = "空间数据字段名")
    private String geom;
    @ApiModelProperty(value = "旧geometry")
    private String oldGeom;
    @ApiModelProperty(value = "修改行新数据")
    private Map<String, Object> newRow;
    @ApiModelProperty(value = "表所有字段名称")
    private List<String> fields;

    public PgRowInfo(String tableName, String geom, String oldGeom, Map<String, Object> newRow, List<String> fields) {
        this.tableName = tableName;
        this.geom = geom;
        this.oldGeom = oldGeom;
        this.newRow = newRow;
        this.fields = fields;
    }

    public PgRowInfo() {
        super();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOldGeom() {
        return oldGeom;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public void setOldGeom(String oldGeom) {
        this.oldGeom = oldGeom;
    }

    public Map<String, Object> getNewRow() {
        return newRow;
    }

    public void setNewRow(Map<String, Object> newRow) {
        this.newRow = newRow;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

}
