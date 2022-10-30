package com.example.webgistest.pojo;

import java.util.Map;

public class FeatureInfo {
    //要素类型
    private String type = "Feature";
    //要素唯一ID
    private long id;
    //要素属性信息
    private Map properties;
    //地理要素信息geojson格式
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
