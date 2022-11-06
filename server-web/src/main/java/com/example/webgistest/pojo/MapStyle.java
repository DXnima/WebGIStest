package com.example.webgistest.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * SLD样式修改参数类
 * 参数类
 */
@ApiModel(value = "SLD样式修改参数")
public class MapStyle {

    // geomType
    @ApiModelProperty(value = "样式类型，点(POINT)、线(LINE)、面(POLYGON)")
    private String geomType;
    // general
    @ApiModelProperty(value = "点、线、面样式，<br/>规则设置名称<br/>参数示例：default rule<br/>", example = "default rule")
    private String name;
    @ApiModelProperty(value = "点样式，<br/>规则设置图片路径<br/>参数示例：https://gitee.com/dxwangnima/images/raw/master/img/20210612155110.png<br/>", example = "https://gitee.com/dxwangnima/images/raw/master/img/20210612155110.png")
    private String path;
    @ApiModelProperty(value = "点样式，<br/>图形形状<br/>参数示例：<br/>可选：cross、circle、triangle、X、star、arrow、hatch、square<br/>", example = "circle")
    private String markname;
    @ApiModelProperty(value = "点样式，<br/>图形大小<br/>参数示例：8", example = "8")
    private String size;
    @ApiModelProperty(value = "点样式，<br/>图形旋转<br/>参数示例：5", example = "5")
    private String rotation;
    @ApiModelProperty(value = "点、线、面样式，<br/>偏移量<br/>参数示例：[1,2,geoName]<br/>", example = "[1,2,'geom']")
    private String[] offset;
    @ApiModelProperty(value = "点、线、面样式，<br/>显示最小比例尺<br/>参数示例：1000.0<br/>", example = "1000.0")
    private String minscale;
    @ApiModelProperty(value = "点、线、面样式，<br/>显示最大比例尺<br/>参数示例：1000000.0<br/>", example = "1000000.0")
    private String maxscale;

    // border
    @ApiModelProperty(value = "点、线、面样式，<br/>是否启用边框<br/>参数示例：false<br/>", example = "false")
    private boolean borderenable;
    @ApiModelProperty(value = "点、线、面样式，<br/>边框宽度<br/>参数示例：1", example = "1")
    private String borderwidth;
    @ApiModelProperty(value = "点、线、面样式，<br/>边框颜色<br/>参数示例：#00008B", example = "#00008B")
    private String bordercolor;
    @ApiModelProperty(value = "点、线、面样式，<br/>边框透明度<br/>参数示例：0.5", example = "0.5")
    private String borderopacity;

    // fill
    @ApiModelProperty(value = "点、面样式，<br/>是否启用填充<br/>参数示例：false<br/>", example = "false")
    private boolean fillenable;
    @ApiModelProperty(value = "点、面样式，<br/>填充颜色<br/>参数示例：#00008B", example = "#00008B")
    private String fillcolor;
    @ApiModelProperty(value = "点、面样式，<br/>填充透明度<br/>参数示例：0.5", example = "0.5")
    private String fillopacity;

    // graphics path
    @ApiModelProperty(value = "线、面样式，<br/>图形路径边框<br/>参数示例：[url链接,宽度,大小]<br/>", example = "['https://gitee.com/dxwangnima/images/raw/master/img/20210612155110.png','2','5']")
    private String[] graphicspathborder;
    @ApiModelProperty(value = "面样式，<br/>图形路径填充<br/>参数示例：[url链接,大小]<br/>", example = "['https://gitee.com/dxwangnima/images/raw/master/img/20210612155110.png','5']")
    private String[] graphicspathfill;

    // wellknown marks for borders and fill; which also need to have width and color
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String wkmgraphicsborder;
    @ApiModelProperty(value = "面样式，<br/>图形填充<br/>参数示例：[图形形状名称,宽度,颜色,大小]<br/>", example = "['circle','2','#00008B','5']")
    private String[] wkmgraphicsfill;

    // dashes and line properties
    @ApiModelProperty(value = "线、面样式，<br/>虚线<br/>参数示例：1,3,5,7,9<br/>", example = "1,3,5")
    private String dash;
    @ApiModelProperty(value = "线、面样式，<br/>虚线偏移量<br/>参数示例：0.5<br/>", example = "0.5")
    private String dashoffset;
    @ApiModelProperty(value = "线、面样式，<br/>线两端的样式<br/>参数示例：<br/>可选：butt、round、square<br/>", example = "round")
    private String linecap;
    @ApiModelProperty(value = "线、面样式，<br/>线条连接的样式<br/>参数示例：<br/>可选：bevel、miter、round<br/>", example = "bevel")
    private String linejoin;
    @ApiModelProperty(value = "线、面样式，<br/>线条终点样式<br/>参数示例：[geomName,点形状,大小,颜色]<br/>", example = "['geomName','circle','2','#00008B']")
    @JsonIgnore
    private String[] lineend;
    @ApiModelProperty(value = "线、面样式，<br/>线条起点样式<br/>参数示例：[geomName,点形状,大小,颜色]<br/>", example = "['geomName','circle','2','#00008B']")
    @JsonIgnore
    private String[] linestart;

    // text
    @ApiModelProperty(value = "点、线、面样式，<br/>是否启用标签<br/>参数示例：false<br/>", example = "false")
    private boolean labelenable;
    @ApiModelProperty(value = "点、线、面样式，<br/>标签内容<br/>参数示例：{values:['default name'],fromField:false}")
    private StyleVariable label;
    @ApiModelProperty(value = "点、线、面样式，<br/>标签字体<br/>参数示例：[字体,字形(italic、normal),大小]<br/>", example = "['宋体','normal','12.0']")
    private String[] labelfont;
    @ApiModelProperty(value = "点、线、面样式，<br/>标签颜色<br/>参数示例：#00008B<br/>", example = "#00008B")
    private String labelcolor;
    @ApiModelProperty(value = "点、线、面样式，<br/>标签光晕颜色<br/>参数示例：#00008B<br/>", example = "#00008B")
    private String labelhalocolor;
    @ApiModelProperty(value = "点、线、面样式，<br/>标签光晕半径<br/>参数示例：1<br/>", example = "1")
    private String labelhaloradius;
    @ApiModelProperty(value = "点、线、面样式，<br/>标签锚点<br/>参数示例：[x,y]<br/>", example = "[1,1]")
    private String[] labelanchor;
    @ApiModelProperty(value = "点、线、面样式，<br/>标签位移<br/>参数示例：[1,1]<br/>", example = "[1,1]")
    private String[] labeldisplacement;
    @ApiModelProperty(value = "点、线、面样式，<br/>标签旋转<br/>参数示例：5", example = "5")
    private String labelrotation;
    @ApiModelProperty(value = "线样式，<br/>标签间隙<br/>参数示例：1<br/>", example = "1")
    private String labelinitialgap;
    @ApiModelProperty(value = "线样式，<br/>标签垂直偏移量<br/>参数示例：1<br/>", example = "1")
    private String labelperpendicularoffset;

    //filters
    @ApiModelProperty(value = "过滤条件<br/>参数示例：name = '武汉'<br/>", example = "name = '武汉'")
    private String filter;

    public MapStyle(String geomType, String name, String path, String markname, String size, String rotation, String[] offset, String minscale, String maxscale, boolean borderenable, String borderwidth, String bordercolor, String borderopacity, boolean fillenable, String fillcolor, String fillopacity, String[] graphicspathborder, String[] graphicspathfill, String wkmgraphicsborder, String[] wkmgraphicsfill, String dash, String dashoffset, String linecap, String linejoin, String[] lineend, String[] linestart, boolean labelenable, StyleVariable label, String[] labelfont, String labelcolor, String labelhalocolor, String labelhaloradius, String[] labelanchor, String[] labeldisplacement, String labelrotation, String labelinitialgap, String labelperpendicularoffset, String filter) {
        this.geomType = geomType;
        this.name = name;
        this.path = path;
        this.markname = markname;
        this.size = size;
        this.rotation = rotation;
        this.offset = offset;
        this.minscale = minscale;
        this.maxscale = maxscale;
        this.borderenable = borderenable;
        this.borderwidth = borderwidth;
        this.bordercolor = bordercolor;
        this.borderopacity = borderopacity;
        this.fillenable = fillenable;
        this.fillcolor = fillcolor;
        this.fillopacity = fillopacity;
        this.graphicspathborder = graphicspathborder;
        this.graphicspathfill = graphicspathfill;
        this.wkmgraphicsborder = wkmgraphicsborder;
        this.wkmgraphicsfill = wkmgraphicsfill;
        this.dash = dash;
        this.dashoffset = dashoffset;
        this.linecap = linecap;
        this.linejoin = linejoin;
        this.lineend = lineend;
        this.linestart = linestart;
        this.labelenable = labelenable;
        this.label = label;
        this.labelfont = labelfont;
        this.labelcolor = labelcolor;
        this.labelhalocolor = labelhalocolor;
        this.labelhaloradius = labelhaloradius;
        this.labelanchor = labelanchor;
        this.labeldisplacement = labeldisplacement;
        this.labelrotation = labelrotation;
        this.labelinitialgap = labelinitialgap;
        this.labelperpendicularoffset = labelperpendicularoffset;
        this.filter = filter;
    }

    public MapStyle() {
        super();
    }

    public String getGeomType() {
        return geomType;
    }

    public void setGeomType(String geomType) {
        this.geomType = geomType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? "" : path;
    }

    public String getMarkname() {
        return markname;
    }

    public void setMarkname(String markname) {
        this.markname = markname == null ? "" : markname;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? "0" : size;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation == null ? "0" : rotation;
    }

    public String[] getOffset() {
        return offset;
    }

    public void setOffset(String[] offset) {
        this.offset = offset;
    }

    public String getMinscale() {
        return minscale;
    }

    public void setMinscale(String minscale) {
        this.minscale = minscale == null ? "0" : minscale;
    }

    public String getMaxscale() {
        return maxscale;
    }

    public void setMaxscale(String maxscale) {
        this.maxscale = maxscale == null ? "0" : maxscale;
    }

    public boolean getBorderenable() {
        return borderenable;
    }

    public void setBorderenable(boolean borderenable) {
        this.borderenable = borderenable;
    }

    public String getBorderwidth() {
        return borderwidth;
    }

    public void setBorderwidth(String borderwidth) {
        this.borderwidth = borderwidth == null ? "0" : borderwidth;
    }

    public String getBordercolor() {
        return bordercolor;
    }

    public void setBordercolor(String bordercolor) {
        this.bordercolor = bordercolor == null ? "" : bordercolor;
    }

    public String getBorderopacity() {
        return borderopacity;
    }

    public void setBorderopacity(String borderopacity) {
        this.borderopacity = borderopacity == null ? "0" : borderopacity;
    }

    public boolean getFillenable() {
        return fillenable;
    }

    public void setFillenable(boolean fillenable) {
        this.fillenable = fillenable;
    }

    public String getFillcolor() {
        return fillcolor;
    }

    public void setFillcolor(String fillcolor) {
        this.fillcolor = fillcolor == null ? "" : fillcolor;
    }

    public String getFillopacity() {
        return fillopacity;
    }

    public void setFillopacity(String fillopacity) {
        this.fillopacity = fillopacity == null ? "0" : fillopacity;
    }

    public String[] getGraphicspathborder() {
        return graphicspathborder;
    }

    public void setGraphicspathborder(String[] graphicspathborder) {
        this.graphicspathborder = graphicspathborder;
    }

    public String[] getGraphicspathfill() {
        return graphicspathfill;
    }

    public void setGraphicspathfill(String[] graphicspathfill) {
        this.graphicspathfill = graphicspathfill;
    }

    public String getWkmgraphicsborder() {
        return wkmgraphicsborder;
    }

    public void setWkmgraphicsborder(String wkmgraphicsborder) {
        this.wkmgraphicsborder = wkmgraphicsborder == null ? "" : wkmgraphicsborder;
    }

    public String[] getWkmgraphicsfill() {
        return wkmgraphicsfill;
    }

    public void setWkmgraphicsfill(String[] wkmgraphicsfill) {
        this.wkmgraphicsfill = wkmgraphicsfill;
    }

    public String getDash() {
        return dash;
    }

    public void setDash(String dash) {
        this.dash = dash == null ? "" : dash;
    }

    public String getDashoffset() {
        return dashoffset;
    }

    public void setDashoffset(String dashoffset) {
        this.dashoffset = dashoffset == null ? "0" : dashoffset;
    }

    public String getLinecap() {
        return linecap;
    }

    public void setLinecap(String linecap) {
        this.linecap = linecap == null ? "" : linecap;
    }

    public String getLinejoin() {
        return linejoin;
    }

    public void setLinejoin(String linejoin) {
        this.linejoin = linejoin == null ? "" : linejoin;
    }

    public String[] getLineend() {
        return lineend;
    }

    public void setLineend(String[] lineend) {
        this.lineend = lineend;
    }

    public String[] getLinestart() {
        return linestart;
    }

    public void setLinestart(String[] linestart) {
        this.linestart = linestart;
    }

    public boolean getLabelenable() {
        return labelenable;
    }

    public void setLabelenable(boolean labelenable) {
        this.labelenable = labelenable;
    }

    public StyleVariable getLabel() {
        return label;
    }

    public void setLabel(StyleVariable label) {
        this.label = label;
    }

    public String[] getLabelfont() {
        return labelfont;
    }

    public void setLabelfont(String[] labelfont) {
        this.labelfont = labelfont;
    }

    public String getLabelcolor() {
        return labelcolor;
    }

    public void setLabelcolor(String labelcolor) {
        this.labelcolor = labelcolor == null ? "" : labelcolor;
    }

    public String getLabelhalocolor() {
        return labelhalocolor;
    }

    public void setLabelhalocolor(String labelhalocolor) {
        this.labelhalocolor = labelhalocolor == null ? "" : labelhalocolor;
    }

    public String getLabelhaloradius() {
        return labelhaloradius;
    }

    public void setLabelhaloradius(String labelhaloradius) {
        this.labelhaloradius = labelhaloradius == null ? "0" : labelhaloradius;
    }

    public String[] getLabelanchor() {
        return labelanchor;
    }

    public void setLabelanchor(String[] labelanchor) {
        this.labelanchor = labelanchor;
    }

    public String[] getLabeldisplacement() {
        return labeldisplacement;
    }

    public void setLabeldisplacement(String[] labeldisplacement) {
        this.labeldisplacement = labeldisplacement;
    }

    public String getLabelrotation() {
        return labelrotation;
    }

    public void setLabelrotation(String labelrotation) {
        this.labelrotation = labelrotation == null ? "0" : labelrotation;
    }

    public String getLabelinitialgap() {
        return labelinitialgap;
    }

    public void setLabelinitialgap(String labelinitialgap) {
        this.labelinitialgap = labelinitialgap == null ? "0" : labelinitialgap;
    }

    public String getLabelperpendicularoffset() {
        return labelperpendicularoffset;
    }

    public void setLabelperpendicularoffset(String labelperpendicularoffset) {
        this.labelperpendicularoffset = labelperpendicularoffset == null ? "0" : labelperpendicularoffset;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter == null ? "" : filter;
    }

}
