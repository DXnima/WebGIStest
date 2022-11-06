package com.example.webgistest.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@ApiModel(value = "读取gdb要素类")
public class GdbFeatureInfo {
    @ApiModelProperty(value = "要素类型")
    private String type = "Feature";
    @ApiModelProperty(value = "要素唯一ID")
    private long id;
    //要素属性信息
    @ApiModelProperty(value = "要素属性信息")
    private Map properties;
    @ApiModelProperty(value = "地理要素信息geojson格式")
    private String geometry;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }
}
