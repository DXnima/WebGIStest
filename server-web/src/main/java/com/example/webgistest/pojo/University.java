package com.example.webgistest.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel(value = "大学表信息类")
public class University {
    @ApiModelProperty(value = "唯一id")
    private Integer gid;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "部门")
    private String department;
    @ApiModelProperty(value = "等级")
    private String schoo1Lev;
    @ApiModelProperty(value = "类型")
    private String types;
    @ApiModelProperty(value = "省份")
    private String province;
    @ApiModelProperty(value = "是否双一流")
    private Integer firstUniv;
    @ApiModelProperty(value = "院系")
    private Integer firstDisc;
    @ApiModelProperty(value = "满意度")
    private Integer graduate;
    @ApiModelProperty(value = "表名")
    private BigDecimal satisfacti;
    @ApiModelProperty(value = "纬度")
    private BigDecimal lon;
    @ApiModelProperty(value = "经度")
    private BigDecimal lat;
    @ApiModelProperty(value = "地理要素信息Geometry类型")
    private Object geom;

    public University(Integer gid, String name, String department, String schoo1Lev, String types, String province, Integer firstUniv, Integer firstDisc, Integer graduate, BigDecimal satisfacti, BigDecimal lon, BigDecimal lat, Object geom) {
        this.gid = gid;
        this.name = name;
        this.department = department;
        this.schoo1Lev = schoo1Lev;
        this.types = types;
        this.province = province;
        this.firstUniv = firstUniv;
        this.firstDisc = firstDisc;
        this.graduate = graduate;
        this.satisfacti = satisfacti;
        this.lon = lon;
        this.lat = lat;
        this.geom = geom;
    }

    public University() {
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public String getSchoo1Lev() {
        return schoo1Lev;
    }

    public void setSchoo1Lev(String schoo1Lev) {
        this.schoo1Lev = schoo1Lev == null ? null : schoo1Lev.trim();
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types == null ? null : types.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public Integer getFirstUniv() {
        return firstUniv;
    }

    public void setFirstUniv(Integer firstUniv) {
        this.firstUniv = firstUniv;
    }

    public Integer getFirstDisc() {
        return firstDisc;
    }

    public void setFirstDisc(Integer firstDisc) {
        this.firstDisc = firstDisc;
    }

    public Integer getGraduate() {
        return graduate;
    }

    public void setGraduate(Integer graduate) {
        this.graduate = graduate;
    }

    public BigDecimal getSatisfacti() {
        return satisfacti;
    }

    public void setSatisfacti(BigDecimal satisfacti) {
        this.satisfacti = satisfacti;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public Object getGeom() {
        return geom;
    }

    public void setGeom(Object geom) {
        this.geom = geom;
    }
}
