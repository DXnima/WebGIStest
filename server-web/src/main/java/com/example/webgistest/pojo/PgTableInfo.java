package com.example.webgistest.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "PostGIS表格信息类")
public class PgTableInfo {

    @ApiModelProperty(value = "表名")
    private String name;
    @ApiModelProperty(value = "类型，包括：POINT(点)、LINE(线)、POLYGON(面)")
    private String type;
    @ApiModelProperty(value = "空间字段名称")
    private String geom;
    @ApiModelProperty(value = "表总行数")
    private String number;

    public PgTableInfo(String name, String type, String geom, String number) {
        this.name = name;
        this.type = type;
        this.geom = geom;
        this.number = number;
    }

    public PgTableInfo() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
