package com.example.webgistest.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * geoserver rest接口
 * 发布Shapefile文件
 * 配置类
 */
@ApiModel(value = "shp图层发布配置类")
public class ShapeFileConfig {

    @ApiModelProperty(value = "工作空间名称")
    private String workspaceName;//待创建和发布图层的工作区名称workspace
    @ApiModelProperty(value = "shp文件的完整路径")
    private String shpFile;//shp文件的完整路径
    @ApiModelProperty(value = "坐标系统代码", example = "4326")
    private Integer crsCode;//坐标系统代码
    @ApiModelProperty(value = "样式工作空间名称")
    private String styleWorkspace;//样式工作空间名
    @ApiModelProperty(value = "样式名称")
    private String styleName;//样式名

    public ShapeFileConfig(String workspaceName, String shpFile, Integer crsCode, String styleWorkspace, String styleName) {
        this.workspaceName = workspaceName;
        this.shpFile = shpFile;
        this.crsCode = crsCode;
        this.styleWorkspace = styleWorkspace;
        this.styleName = styleName;
    }

    public ShapeFileConfig() {
        super();
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public String getShpFile() {
        return shpFile;
    }

    public void setShpFile(String shpFile) {
        this.shpFile = shpFile;
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
