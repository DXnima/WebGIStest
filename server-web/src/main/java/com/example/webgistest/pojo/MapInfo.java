package com.example.webgistest.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 图层详情信息获取
 * 信息类
 */
@ApiModel(value = "图层详情信息获取类")
public class MapInfo {

    @ApiModelProperty(value = "名称")
    String name;
    @ApiModelProperty(value = "标题")
    String title;
    @ApiModelProperty(value = "摘要")
    String abstracts;
    @ApiModelProperty(value = "坐标系统代码", example = "4326")
    String crs;
    @ApiModelProperty(value = "类型")
    String type;
    @ApiModelProperty(value = "工作空间名称")
    String workspace;
    @ApiModelProperty(value = "样式名称")
    String style;
    @ApiModelProperty(value = "图层属性字段")
    List<String> attributes;
    @ApiModelProperty(value = "工作空间URI")
    String uri;
    @ApiModelProperty(value = "geoserverURI")
    String restURI;

    public MapInfo(String name, String title, String abstracts, String crs, String type, String workspace, String style, List<String> attributes, String uri, String restURI) {
        this.name = name;
        this.title = title;
        this.abstracts = abstracts;
        this.crs = crs;
        this.type = type;
        this.workspace = workspace;
        this.style = style;
        this.attributes = attributes;
        this.uri = uri;
        this.restURI = restURI;
    }

    public MapInfo() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRestURI() {
        return restURI;
    }

    public void setRestURI(String restURI) {
        this.restURI = restURI;
    }

}
