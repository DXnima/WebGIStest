package com.example.webgistest.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "读取gdb图层信息类")
public class GdbLayerInfo {
    @ApiModelProperty(value = "图层名称")
    private String name;
    @ApiModelProperty(value = "坐标系EPSG代码")
    private String proj;
    @ApiModelProperty(value = "图层要素个数")
    private long count;
    @ApiModelProperty(value = "要素信息")
    private List<GdbFeatureInfo> features;

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

    public List<GdbFeatureInfo> getFeatures() {
        return features;
    }

    public void setFeatures(List<GdbFeatureInfo> features) {
        this.features = features;
    }
}
