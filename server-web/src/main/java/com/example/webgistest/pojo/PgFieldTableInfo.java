package com.example.webgistest.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "数据库表字段信息获取类")
public class PgFieldTableInfo {

    @ApiModelProperty(value = "表名")
    private String tableName;
    @ApiModelProperty(value = "空间数据字段名")
    private String geom;
    @ApiModelProperty(value = "查询内容")
    private String query;
    @ApiModelProperty(value = "页数")
    private int pageNum;
    @ApiModelProperty(value = "每页数据量")
    private int pagesize;
    @ApiModelProperty(value = "表所有字段")
    private List<String> fields;

    public PgFieldTableInfo(String tableName, String geom, String query, int pageNum, int pagesize, List<String> fields) {
        this.tableName = tableName;
        this.geom = geom;
        this.query = query;
        this.pageNum = pageNum;
        this.pagesize = pagesize;
        this.fields = fields;
    }

    public PgFieldTableInfo() {
        super();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pagesize;
    }

    public void setPageSize(int pagesize) {
        this.pagesize = pagesize;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

}
