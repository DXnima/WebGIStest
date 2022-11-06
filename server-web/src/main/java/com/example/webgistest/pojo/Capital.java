package com.example.webgistest.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "读取gdb要素类")
public class Capital {
    @ApiModelProperty(value = "唯一id")
    private Integer gid;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "经度")
    private Double lat;
    @ApiModelProperty(value = "纬度")
    private Double lon;
    @ApiModelProperty(value = "地理要素信息Geometry类型 WKT", example = "POINT(129.345, 34.6589)")
    private Object geom;

    public Capital(Integer gid, String name, Double lat, Double lon, Object geom) {
        this.gid = gid;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.geom = geom;
    }

    public Capital() {
        super();
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Object getGeom() {
        return geom;
    }

    public void setGeom(Object geom) {
        this.geom = geom;
    }
}