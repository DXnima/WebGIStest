package com.example.webgistest.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "layer_edit表信息")
public class LayerEdit {
    @ApiModelProperty(value = "唯一id")
    private Integer id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "地理要素信息Geometry类型 WKT", example = "POINT(129.345, 34.6589)")
    private Object geom;

    public LayerEdit(Integer id, String name, Object geom) {
        this.id = id;
        this.name = name;
        this.geom = geom;
    }

    public LayerEdit() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Object getGeom() {
        return geom;
    }

    public void setGeom(Object geom) {
        this.geom = geom;
    }
}