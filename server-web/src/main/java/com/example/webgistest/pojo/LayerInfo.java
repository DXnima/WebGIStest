package com.example.webgistest.pojo;

import java.util.List;

public class LayerInfo {
    //图层名称
    private String name;
    //坐标系EPSG代码
    private String proj;
    //图层要素个数
    private long count;
    //要素信息
    private List<FeatureInfo> features;

    public String getProj() {
        return proj;
    }

    public void setProj(String proj) {
        this.proj = proj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<FeatureInfo> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatureInfo> features) {
        this.features = features;
    }
}
