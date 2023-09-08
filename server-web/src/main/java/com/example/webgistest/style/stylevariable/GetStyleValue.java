package com.example.webgistest.style.stylevariable;

import com.example.webgistest.exception.ErrorException;
import com.example.webgistest.pojo.MapStyle;
import com.example.webgistest.pojo.StyleVariable;
import com.example.webgistest.style.styleattribute.*;
import org.geotools.styling.Graphic;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

import java.lang.reflect.Field;
import java.net.MalformedURLException;

/**
 * 根据SLD获取样式参数类
 *
 * @author wnm
 * @date 2021/7/7
 */
public class GetStyleValue {

    private RuleWrapper ruleWrapper;

    private final MapStyle mapStyle = new MapStyle();

    private final StyleVariable value = new StyleVariable();

    public GetStyleValue(RuleWrapper ruleWrapper) {
        this.ruleWrapper = ruleWrapper;
    }

    public void setRuleWrapper(RuleWrapper ruleWrapper) {
        this.ruleWrapper = ruleWrapper;
    }

    public RuleWrapper getRuleWrapper() {
        return ruleWrapper;
    }

    public MapStyle getStyleValue() throws ErrorException {
        String type = ruleWrapper.getType();
        mapStyle.setSize("0");
        mapStyle.setRotation("0");
        mapStyle.setDashoffset("0");
        mapStyle.setFillopacity("0");
        mapStyle.setLabelinitialgap("0");
        mapStyle.setLabelperpendicularoffset("0");
        mapStyle.setGraphicspathborder(new String[]{"", "0", "0"});
        mapStyle.setGraphicspathfill(new String[]{"", "0"});
        mapStyle.setWkmgraphicsfill(new String[]{"", "0", "", "0"});
        mapStyle.setLineend(new String[]{"", "", "", ""});
        mapStyle.setLinestart(new String[]{"", "", "", ""});
        mapStyle.setLabelanchor(new String[]{"0", "0"});
        mapStyle.setLabeldisplacement(new String[]{"0", "0"});
        mapStyle.setLabelfont(new String[]{"", "", "0"});
        switch (type) {
            case "POINT":
                getStyleValueByPoint();
                break;
            case "LINE":
                getStyleValueByLine();
                break;
            case "POLYGON":
                getStyleValueByPolygon();
                break;
            default:
                break;
        }
        mapStyle.setGeomType(type);
        return mapStyle;
    }

    public void getStyleValueByPoint() throws ErrorException {

        PointSymbolizerWrapper pointSymbolizerWrapper = ruleWrapper.getGeometrySymbolizersWrapper().adapt(
                PointSymbolizerWrapper.class);
        TextSymbolizerWrapper textSymbolizerWrapper = ruleWrapper.getTextSymbolizersWrapper();

        for (Field field : mapStyle.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            switch (StyleEventType.getStyleEventType(field.getName())) {

                // GENERAL PARAMETERS
                case NAME:
                    mapStyle.setName(ruleWrapper.getName());
                    break;
                case SIZE: {
                    String value = pointSymbolizerWrapper.getSize();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setSize(value);
                    break;
                }
                case ROTATION: {
                    String value = pointSymbolizerWrapper.getRotation();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setRotation(value);
                    break;
                }
                case OFFSET: {
                    String offsetX = pointSymbolizerWrapper.getxOffset();
                    if (offsetX == null || offsetX.equals("")) {
                        offsetX = "0";
                    }
                    String offsetY = pointSymbolizerWrapper.getyOffset();
                    if (offsetY == null || offsetY.equals("")) {
                        offsetY = "0";
                    }
                    String geoName = pointSymbolizerWrapper.getGeoName();
                    mapStyle.setOffset(new String[]{offsetX, offsetY, geoName});
                    break;
                }
                case MAXSCALE: {
                    String value = ruleWrapper.getMaxScale();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setMaxscale(value);
                    break;
                }
                case MINSCALE: {
                    String value = ruleWrapper.getMinScale();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setMinscale(value);
                    break;
                }
                case MARKNAME:
                    mapStyle.setMarkname(pointSymbolizerWrapper.getMarkName());
                    break;
                case PATH:
                    try {
                        mapStyle.setPath(pointSymbolizerWrapper.getExternalGraphicPath());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        throw new ErrorException("图片样式参数获取失败！");
                    }
                    break;
                // BORDER PARAMETERS
                case BORDERENABLE:
                    mapStyle.setBorderenable(pointSymbolizerWrapper.hasStroke());
                    break;
                case BORDERWIDTH: {
                    String value = pointSymbolizerWrapper.getStrokeWidth();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setBorderwidth(value);
                    break;
                }
                case BORDERCOLOR: {
                    mapStyle.setBordercolor(pointSymbolizerWrapper.getStrokeColor());
                    break;
                }
                case BORDEROPACITY: {
                    String value = pointSymbolizerWrapper.getStrokeOpacity();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setBorderopacity(value);
                    break;
                }
                // FILL PARAMETERS
                case FILLENABLE:
                    mapStyle.setFillenable(pointSymbolizerWrapper.hasFill());
                    break;
                case FILLCOLOR: {
                    mapStyle.setFillcolor(pointSymbolizerWrapper.getFillColor());
                    break;
                }
                case FILLOPACITY: {
                    String value = pointSymbolizerWrapper.getFillOpacity();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setFillopacity(value);
                    break;
                }
                // LABEL PARAMETERS
                case LABELENABLE: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabelenable(false);
                    } else {
                        mapStyle.setLabelenable(true);
                    }
                    break;
                }
                case LABEL: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabel(value);
                        break;
                    }
                    value.setFromField(getLabelFromField(textSymbolizerWrapper));
                    value.setValues(new String[]{textSymbolizerWrapper.getLabelName()});
                    mapStyle.setLabel(value);
                    break;
                }
                case LABELFONT: {
                    if (textSymbolizerWrapper == null) {
                        break;
                    }
                    String name = textSymbolizerWrapper.getFontFamily();
                    String style = textSymbolizerWrapper.getFontStyle();
                    String size = textSymbolizerWrapper.getFontSize();
                    if (size == null || size.equals("")) {
                        size = "0";
                    }
                    mapStyle.setLabelfont(new String[]{name, style, size});
                    break;
                }
                case LABELCOLOR: {
                    if (textSymbolizerWrapper == null) {
                        break;
                    }
                    mapStyle.setLabelcolor(textSymbolizerWrapper.getColor());
                    break;
                }
                case LABELHALOCOLOR: {
                    if (textSymbolizerWrapper == null) {
                        break;
                    }
                    mapStyle.setLabelhalocolor(textSymbolizerWrapper.getHaloColor());
                    break;
                }
                case LABELHALORADIUS: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabelhaloradius("0");
                        break;
                    }
                    String value = textSymbolizerWrapper.getHaloRadius();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setLabelhaloradius(value);
                    break;
                }
                case LABELANCHOR: {
                    if (textSymbolizerWrapper == null) {
                        break;
                    }
                    String x = textSymbolizerWrapper.getAnchorX();
                    if (x == null || x.equals("")) {
                        x = "0";
                    }
                    String y = textSymbolizerWrapper.getAnchorY();
                    if (y == null || y.equals("")) {
                        y = "0";
                    }
                    mapStyle.setLabelanchor(new String[]{x, y});
                    break;
                }
                case LABELDISPLACEMENT: {
                    if (textSymbolizerWrapper == null) {
                        break;
                    }
                    String x = textSymbolizerWrapper.getDisplacementX();
                    if (x == null || x.equals("")) {
                        x = "0";
                    }
                    String y = textSymbolizerWrapper.getDisplacementY();
                    if (y == null || y.equals("")) {
                        y = "0";
                    }
                    mapStyle.setLabeldisplacement(new String[]{x, y});
                    break;
                }
                case LABELROTATION: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabelrotation("0");
                        break;
                    }
                    String value = textSymbolizerWrapper.getRotation();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setLabelrotation(value);
                    break;
                }
                case FILTER: {
                    if (ruleWrapper.getRule().getFilter() == null) {
                        break;
                    }
                    mapStyle.setFilter(ruleWrapper.getRule().getFilter().toString());
                    break;
                }
                default:
                    break;
            }
        }
    }

    public void getStyleValueByLine() throws ErrorException {

        LineSymbolizerWrapper lineSymbolizerWrapper = ruleWrapper.getGeometrySymbolizersWrapper().adapt(
                LineSymbolizerWrapper.class);

        PointSymbolizerWrapper point = lineSymbolizerWrapper.getStartPointStyle();

        TextSymbolizerWrapper textSymbolizerWrapper = ruleWrapper.getTextSymbolizersWrapper();

        for (Field field : mapStyle.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            switch (StyleEventType.getStyleEventType(field.getName())) {
                // GENERAL PARAMETERS
                case NAME:
                    mapStyle.setName(ruleWrapper.getName());
                    break;
                case OFFSET:
                    String x = lineSymbolizerWrapper.getxOffset();
                    if (x == null || x.equals("")) {
                        x = "0";
                    }
                    String y = lineSymbolizerWrapper.getyOffset();
                    if (y == null || y.equals("")) {
                        y = "0";
                    }
                    String geoName = lineSymbolizerWrapper.getGeoName();
                    mapStyle.setOffset(new String[]{x, y, geoName});
                    break;
                case MAXSCALE: {
                    String value = ruleWrapper.getMaxScale();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setMaxscale(value);
                    break;
                }
                case MINSCALE: {
                    String value = ruleWrapper.getMinScale();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setMinscale(value);
                    break;
                }
                // BORDER PARAMETERS
                case BORDERENABLE: {
                    mapStyle.setBorderenable(lineSymbolizerWrapper.hasStroke());
                    break;
                }
                case BORDERWIDTH: {
                    String value = lineSymbolizerWrapper.getStrokeWidth();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setBorderwidth(value);
                    break;
                }
                case BORDERCOLOR: {
                    mapStyle.setBordercolor(lineSymbolizerWrapper.getStrokeColor());
                    break;
                }
                case BORDEROPACITY: {
                    String value = lineSymbolizerWrapper.getStrokeOpacity();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setBorderopacity(value);
                    break;
                }
                case GRAPHICSPATHBORDER: {
                    try {
                        String url = lineSymbolizerWrapper.getStrokeExternalGraphicStrokePath();
                        String width = "0", size = "0";
                        Graphic graphic = lineSymbolizerWrapper.getStrokeGraphicStroke();
                        if (graphic != null) {
                            width = graphic.getGap().toString();
                            if (width == null || width.equals("")) {
                                width = "0";
                            }
                            size = graphic.getSize().toString();
                            if (size == null || size.equals("")) {
                                size = "0";
                            }
                        }
                        mapStyle.setGraphicspathborder(new String[]{url, width, size});
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        throw new ErrorException("图片边框样式参数获取失败！");
                    }
                    break;
                }
                case DASH: {
                    mapStyle.setDash(lineSymbolizerWrapper.getDash());
                    break;
                }
                case DASHOFFSET: {
                    String value = lineSymbolizerWrapper.getDashOffset();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setDashoffset(value);
                    break;
                }
                case LINECAP: {
                    mapStyle.setLinecap(lineSymbolizerWrapper.getLineCap());
                    break;
                }
                case LINEJOIN: {
                    mapStyle.setLinejoin(lineSymbolizerWrapper.getLineJoin());
                    break;
                }
                case LINEEND: {
                    String geomName = "", markName = "", size = "", color = "";
                    if (point != null) {
                        geomName = point.getSymbolizer().getGeometryPropertyName();
                        markName = point.getMarkName();
                        size = point.getSize();
                        color = point.getStrokeColor();
                    }
                    mapStyle.setLineend(new String[]{geomName, markName, size, color});
                    break;
                }
                case LINESTART: {
                    String geomName = "", markName = "", size = "", color = "";
                    if (point != null) {
                        geomName = point.getSymbolizer().getGeometryPropertyName();
                        markName = point.getMarkName();
                        size = point.getSize();
                        color = point.getStrokeColor();
                    }
                    mapStyle.setLinestart(new String[]{geomName, markName, size, color});
                    break;
                }
                // LABEL PARAMETERS
                case LABELENABLE: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabelenable(false);
                    } else {
                        mapStyle.setLabelenable(true);
                    }
                    break;
                }
                case LABEL: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabel(value);
                        break;
                    }
                    value.setFromField(getLabelFromField(textSymbolizerWrapper));
                    value.setValues(new String[]{textSymbolizerWrapper.getLabelName()});
                    mapStyle.setLabel(value);
                    break;
                }
                case LABELFONT: {
                    if (textSymbolizerWrapper == null) {
                        break;
                    }
                    String name = textSymbolizerWrapper.getFontFamily();
                    String style = textSymbolizerWrapper.getFontStyle();
                    String size = textSymbolizerWrapper.getFontSize();
                    if (size == null || size.equals("")) {
                        size = "0";
                    }
                    mapStyle.setLabelfont(new String[]{name, style, size});
                    break;
                }
                case LABELCOLOR: {
                    if (textSymbolizerWrapper == null) {
                        break;
                    }
                    mapStyle.setLabelcolor(textSymbolizerWrapper.getColor());
                    break;
                }
                case LABELHALOCOLOR: {
                    if (textSymbolizerWrapper == null) {
                        break;
                    }
                    mapStyle.setLabelhalocolor(textSymbolizerWrapper.getHaloColor());
                    break;
                }
                case LABELHALORADIUS: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabelhaloradius("0");
                        break;
                    }
                    String value = textSymbolizerWrapper.getHaloRadius();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setLabelhaloradius(value);
                    break;
                }
                case LABELINITIALGAP: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabelinitialgap("0");
                        break;
                    }
                    String value = textSymbolizerWrapper.getInitialGap();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setLabelinitialgap(value);
                    break;
                }
                case LABELPERPENDICULAROFFSET: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabelperpendicularoffset("0");
                        break;
                    }
                    String value = textSymbolizerWrapper.getPerpendicularOffset();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setLabelperpendicularoffset(value);
                    break;
                }
                case LABELROTATION: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabelrotation("0");
                        break;
                    }
                    String value = textSymbolizerWrapper.getRotation();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setLabelrotation(value);
                    break;
                }
                case FILTER: {
                    if (ruleWrapper.getRule().getFilter() == null) {
                        break;
                    }
                    mapStyle.setFilter(ruleWrapper.getRule().getFilter().toString());
                    break;
                }

                default:
                    break;
            }
        }
    }

    public void getStyleValueByPolygon() throws ErrorException {

        PolygonSymbolizerWrapper polygonSymbolizerWrapper = ruleWrapper.getGeometrySymbolizersWrapper().adapt(
                PolygonSymbolizerWrapper.class);

        PointSymbolizerWrapper point = polygonSymbolizerWrapper.getEndPointStyle();

        TextSymbolizerWrapper textSymbolizerWrapper = ruleWrapper.getTextSymbolizersWrapper();

        for (Field field : mapStyle.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            switch (StyleEventType.getStyleEventType(field.getName())) {
                // GENERAL PARAMETERS
                case NAME:
                    mapStyle.setName(ruleWrapper.getName());
                    break;
                case OFFSET: {
                    String offsetX = polygonSymbolizerWrapper.getxOffset();
                    if (offsetX == null || offsetX.equals("")) {
                        offsetX = "0";
                    }
                    String offsetY = polygonSymbolizerWrapper.getyOffset();
                    if (offsetY == null || offsetY.equals("")) {
                        offsetY = "0";
                    }
                    String geoName = polygonSymbolizerWrapper.getGeoName();
                    mapStyle.setOffset(new String[]{offsetX, offsetY, geoName});
                    break;
                }
                case MAXSCALE: {
                    String value = ruleWrapper.getMaxScale();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setMaxscale(value);
                    break;
                }
                case MINSCALE: {
                    String value = ruleWrapper.getMinScale();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setMinscale(value);
                    break;
                }
                // BORDER PARAMETERS
                // BORDER PARAMETERS
                case BORDERENABLE: {
                    mapStyle.setBorderenable(polygonSymbolizerWrapper.hasStroke());
                    break;
                }
                case BORDERWIDTH: {
                    String value = polygonSymbolizerWrapper.getStrokeWidth();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setBorderwidth(value);
                    break;
                }
                case BORDERCOLOR: {
                    mapStyle.setBordercolor(polygonSymbolizerWrapper.getStrokeColor());
                    break;
                }
                case BORDEROPACITY: {
                    String value = polygonSymbolizerWrapper.getStrokeOpacity();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setBorderopacity(value);
                    break;
                }
                case GRAPHICSPATHBORDER: {
                    try {
                        String url = polygonSymbolizerWrapper.getStrokeExternalGraphicStrokePath();
                        Graphic graphic = polygonSymbolizerWrapper.getStrokeGraphicStroke();
                        String width = "", size = "";
                        if (graphic != null) {
                            width = graphic.getGap().toString();
                            if (width == null || width.equals("")) {
                                width = "0";
                            }
                            size = graphic.getSize().toString();
                            if (size == null || size.equals("")) {
                                size = "0";
                            }
                        }
                        mapStyle.setGraphicspathborder(new String[]{url, width, size});
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        throw new ErrorException("图片边框样式参数获取失败！");
                    }
                    break;
                }
                case DASH: {
                    mapStyle.setDash(polygonSymbolizerWrapper.getDash());
                    break;
                }
                case DASHOFFSET: {
                    String value = polygonSymbolizerWrapper.getDashOffset();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setDashoffset(value);
                    break;
                }
                case LINECAP: {
                    mapStyle.setLinecap(polygonSymbolizerWrapper.getLineCap());
                    break;
                }
                case LINEJOIN: {
                    mapStyle.setLinejoin(polygonSymbolizerWrapper.getLineJoin());
                    break;
                }
                case LINEEND: {
                    String geomName = "", markName = "", size = "", color = "";
                    if (point != null) {
                        geomName = point.getSymbolizer().getGeometryPropertyName();
                        markName = point.getMarkName();
                        size = point.getSize();
                        color = point.getStrokeColor();
                    }
                    mapStyle.setLineend(new String[]{geomName, markName, size, color});
                    break;
                }
                case LINESTART: {
                    String geomName = "", markName = "", size = "", color = "";
                    if (point != null) {
                        geomName = point.getSymbolizer().getGeometryPropertyName();
                        markName = point.getMarkName();
                        size = point.getSize();
                        color = point.getStrokeColor();
                    }
                    mapStyle.setLinestart(new String[]{geomName, markName, size, color});
                    break;
                }
                // FILL PARAMETERS
                case FILLENABLE: {
                    mapStyle.setFillenable(polygonSymbolizerWrapper.hasFill());
                    break;
                }
                case FILLCOLOR: {
                    mapStyle.setFillcolor(polygonSymbolizerWrapper.getFillColor());
                    break;
                }
                case FILLOPACITY: {
                    String value = polygonSymbolizerWrapper.getFillOpacity();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setFillopacity(value);
                    break;
                }
                case WKMGRAPHICSFILL: {
                    String wkmname = polygonSymbolizerWrapper.getWkMarkNameFill();
                    String wkmwidth = polygonSymbolizerWrapper.getWkMarkWidthFill();
                    if (wkmwidth == null || wkmwidth.equals("")) {
                        wkmwidth = "0";
                    }
                    String wkmcolor = polygonSymbolizerWrapper.getWkMarkColorFill();
                    String wkmsize = polygonSymbolizerWrapper.getWkMarkSizeFill();
                    if (wkmsize == null || wkmsize.equals("")) {
                        wkmsize = "0";
                    }
                    mapStyle.setWkmgraphicsfill(new String[]{wkmname, wkmwidth, wkmcolor, wkmsize});
                    break;
                }
                case GRAPHICSPATHFILL: {
                    try {
                        String path = polygonSymbolizerWrapper.getFillExternalGraphicFillPath();
                        String size = "";
                        Graphic graphic = polygonSymbolizerWrapper.getFillGraphicFill();
                        if (graphic != null) {
                            size = graphic.getSize().toString();
                            if (size == null || size.equals("")) {
                                size = "0";
                            }
                        }
                        mapStyle.setGraphicspathfill(new String[]{path, size});
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        throw new ErrorException("图片填充样式参数获取失败！");
                    }
                    break;
                }
                // LABEL PARAMETERS
                case LABELENABLE: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabelenable(false);
                    } else {
                        mapStyle.setLabelenable(true);
                    }
                    break;
                }
                case LABEL: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabel(value);
                        break;
                    }
                    value.setFromField(getLabelFromField(textSymbolizerWrapper));
                    value.setValues(new String[]{textSymbolizerWrapper.getLabelName()});
                    mapStyle.setLabel(value);
                    break;
                }
                case LABELFONT: {
                    if (textSymbolizerWrapper == null) {
                        break;
                    }
                    String name = textSymbolizerWrapper.getFontFamily();
                    String style = textSymbolizerWrapper.getFontStyle();
                    String size = textSymbolizerWrapper.getFontSize();
                    if (size == null || size.equals("")) {
                        size = "0";
                    }
                    mapStyle.setLabelfont(new String[]{name, style, size});
                    break;
                }
                case LABELCOLOR: {
                    if (textSymbolizerWrapper == null) {
                        break;
                    }
                    mapStyle.setLabelcolor(textSymbolizerWrapper.getColor());
                    break;
                }
                case LABELHALOCOLOR: {
                    if (textSymbolizerWrapper == null) {
                        break;
                    }
                    mapStyle.setLabelhalocolor(textSymbolizerWrapper.getHaloColor());
                    break;
                }
                case LABELHALORADIUS: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabelhaloradius("0");
                        break;
                    }
                    String value = textSymbolizerWrapper.getHaloRadius();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setLabelhaloradius(value);
                    break;
                }
                case LABELROTATION: {
                    if (textSymbolizerWrapper == null) {
                        mapStyle.setLabelrotation("0");
                        break;
                    }
                    String value = textSymbolizerWrapper.getRotation();
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapStyle.setLabelrotation(value);
                    break;
                }
                case FILTER: {
                    if (ruleWrapper.getRule().getFilter() == null) {
                        break;
                    }
                    mapStyle.setFilter(ruleWrapper.getRule().getFilter().toString());
                    break;
                }
                default:
                    break;
            }
        }
    }

    /**
     * 判断TextSymbolizer的label属性是否来自属性字段
     *
     * @param text
     * @return boolean
     * @author wnm
     * @date 2021/7/7
     */
    public boolean getLabelFromField(TextSymbolizerWrapper text) {
        TextSymbolizer textSymbolizer = (TextSymbolizer) text.getSymbolizer();
        Expression label = textSymbolizer.getLabel();
        if (label != null) {
            return label instanceof PropertyName;
        } else {
            return false;
        }
    }

}
