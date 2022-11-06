package com.example.webgistest.pojo;

import com.example.webgistest.geoserver.ImprovePostGISDatastore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * geoserver rest接口
 * 连接Postgis数据库，发布地图
 * 配置类
 */
@ApiModel(value = "PostGIS图层发布配置类")
public class PgEdit {

    @ApiModelProperty(value = "PostGIS数据库配置")
    private ImprovePostGISDatastore postGIS;//postGIS连接信息
    @ApiModelProperty(value = "工作空间名称")
    private String workspaceName;//工作空间名称
    @ApiModelProperty(value = "表名")
    private String tableName;//表名
    @ApiModelProperty(value = "坐标系统代码", example = "4326")
    private Integer crsCode;//坐标系统代码
    @ApiModelProperty(value = "样式工作空间名称")
    private String styleWorkspace;//样式工作空间名
    @ApiModelProperty(value = "样式名称")
    private String styleName;//样式名

    public PgEdit(ImprovePostGISDatastore postGIS, String workspaceName, String tableName, Integer crsCode, String styleWorkspace, String styleName) {
        this.postGIS = postGIS;
        this.workspaceName = workspaceName;
        this.tableName = tableName;
        this.crsCode = crsCode;
        this.styleWorkspace = styleWorkspace;
        this.styleName = styleName;
    }

    public PgEdit() {
        super();
    }

    public ImprovePostGISDatastore getPostGIS() {
        return postGIS;
    }

    public void setPostGIS(ImprovePostGISDatastore postGIS) {
        this.postGIS = postGIS;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getCrsCode() {
        return crsCode;
    }

    public void setCrsCode(Integer crsCode) {
        this.crsCode = crsCode;
    }

    public String getStyleWorkspace() {
        return styleWorkspace;
    }

    public void setStyleWorkspace(String styleWorkspace) {
        this.styleWorkspace = styleWorkspace;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

}
