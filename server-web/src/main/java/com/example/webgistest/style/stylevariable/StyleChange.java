package com.example.webgistest.style.stylevariable;

import com.example.webgistest.exception.ErrorException;
import com.example.webgistest.pojo.MapStyle;
import com.example.webgistest.pojo.StyleVariable;
import com.example.webgistest.style.styleattribute.*;
import com.example.webgistest.style.utils.StyleUtil;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.Filter;

import java.lang.reflect.Field;
import java.net.MalformedURLException;

/**
 * 根据参数修改SLD样式类
 *
 * @author wnm
 * @date 2021/7/7
 */
public class StyleChange {

    private RuleWrapper ruleWrapper;

    public StyleChange(RuleWrapper ruleWrapper) {
        this.ruleWrapper = ruleWrapper;
    }

    public void setRuleWrapper(RuleWrapper ruleWrapper) {
        this.ruleWrapper = ruleWrapper;
    }

    public RuleWrapper getRuleWrapper() {
        return ruleWrapper;
    }

    public void onStyleChanged(String[] values, boolean fromField, StyleEventType styleEventType) throws ErrorException {
        switch (ruleWrapper.getType()) {
            case "POINT":
                onStyleChangedByPoint(values, fromField, styleEventType);
                break;
            case "LINE":
                onStyleChangedByLine(values, fromField, styleEventType);
                break;
            case "POLYGON":
                onStyleChangedByPolygon(values, fromField, styleEventType);
                break;
        }
    }

    public void onStyleChangedByPoint(String[] values, boolean fromField, StyleEventType styleEventType) throws ErrorException {
        String value = "";
        if (values.length > 0) {
            value = values[0];
        }

        PointSymbolizerWrapper pointSymbolizerWrapper = ruleWrapper.getGeometrySymbolizersWrapper().adapt(
                PointSymbolizerWrapper.class);
        TextSymbolizerWrapper textSymbolizerWrapper = ruleWrapper.getTextSymbolizersWrapper();

        switch (styleEventType) {
            // GENERAL PARAMETERS
            case NAME:
                ruleWrapper.setName(value);
                break;
            case SIZE:
                pointSymbolizerWrapper.setSize(value, fromField);
                break;
            case ROTATION:
                pointSymbolizerWrapper.setRotation(value, fromField);
                break;
            case OFFSET:
                pointSymbolizerWrapper.setOffset(values[0] + "," + values[1] + "," + values[2]);
                break;
            case MAXSCALE:
                ruleWrapper.setMaxScale(value);
                break;
            case MINSCALE:
                ruleWrapper.setMinScale(value);
                break;
            // BORDER PARAMETERS
            case BORDERENABLE: {
                boolean enabled = Boolean.parseBoolean(value);
                if (enabled) {
                    pointSymbolizerWrapper.setHasStroke(enabled);
                }
                break;
            }
            case BORDERWIDTH: {
                pointSymbolizerWrapper.setStrokeWidth(value, fromField);
                break;
            }
            case BORDERCOLOR: {
                pointSymbolizerWrapper.setStrokeColor(value);
                break;
            }
            case BORDEROPACITY: {
                pointSymbolizerWrapper.setStrokeOpacity(value, fromField);
                break;
            }
            // FILL PARAMETERS
            case FILLENABLE: {
                boolean enabled = Boolean.parseBoolean(value);
                if (enabled) {
                    pointSymbolizerWrapper.setHasFill(true);
                }
                break;
            }
            case FILLCOLOR: {
                pointSymbolizerWrapper.setFillColor(value);
                break;
            }
            case FILLOPACITY: {
                pointSymbolizerWrapper.setFillOpacity(value, fromField);
                break;
            }
            // LABEL PARAMETERS
            case LABELENABLE: {
                boolean doEnable = Boolean.parseBoolean(value);
                if (doEnable) {
                    if (textSymbolizerWrapper == null) {
                        TextSymbolizer textSymbolizer = StyleUtil.createDefaultTextSymbolizer("POINT");
                        ruleWrapper.addSymbolizer(textSymbolizer, TextSymbolizerWrapper.class);
                    }
                } else {
                    ruleWrapper.removeTextSymbolizersWrapper();
                }
                break;
            }
            case LABEL: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setLabelName(value, fromField);
                break;
            }
            case LABELFONT: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                String name = values[0];
                String style = values[1];
                double height = Double.parseDouble(values[2]);
                Font font = StyleUtil.sb.createFont(name, style.equals("italic"), style.equals("normal"), height);

                textSymbolizerWrapper.setFont(font);
                break;
            }
            case LABELCOLOR: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setColor(value);
                break;
            }
            case LABELHALOCOLOR: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setHaloColor(value);
                break;
            }
            case LABELHALORADIUS: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setHaloRadius(value);
                break;
            }
            case LABELANCHOR: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setAnchorX(values[0]);
                textSymbolizerWrapper.setAnchorY(values[1]);
                break;
            }
            case LABELDISPLACEMENT: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setDisplacement(values[0] + "," + values[1]);
                break;
            }
            case LABELROTATION: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setRotation(value, fromField);
                break;
            }
            case LABELMAXDISPLACEMENT_VO: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setMaxDisplacementVO(value);
                break;
            }
            case LABELAUTOWRAP_VO: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setAutoWrapVO(value);
                break;
            }
            case LABELSPACEAROUND_VO: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setSpaceAroundVO(value);
                break;
            }
            case FILTER: {
                if (value.length() > 0) {
                    try {
                        Filter filter = ECQL.toFilter(value);
                        ruleWrapper.getRule().setFilter(filter);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ErrorException("样式过滤设置失败！");
                    }
                }
                break;
            }
            case MARKNAME:
                pointSymbolizerWrapper.setMarkName(value);
                break;
            case PATH:
                try {
                    if (value.equals("")) {
                        break;
                    }
                    pointSymbolizerWrapper.setExternalGraphicPath(value);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    throw new ErrorException("图片样式设置失败！");
                }
                break;
            default:
                break;
        }
    }

    public void onStyleChangedByLine(String[] values, boolean fromField, StyleEventType styleEventType) throws ErrorException {
        String value = "";
        if (values.length > 0) {
            value = values[0];
        }

        LineSymbolizerWrapper lineSymbolizerWrapper = ruleWrapper.getGeometrySymbolizersWrapper().adapt(
                LineSymbolizerWrapper.class);

        TextSymbolizerWrapper textSymbolizerWrapper = ruleWrapper.getTextSymbolizersWrapper();

        switch (styleEventType) {
            // GENERAL PARAMETERS
            case NAME:
                ruleWrapper.setName(value);
                break;
            case OFFSET:
                lineSymbolizerWrapper.setOffset(values[0] + "," + values[1] + "," + values[2]);
                break;
            case MAXSCALE:
                ruleWrapper.setMaxScale(value);
                break;
            case MINSCALE:
                ruleWrapper.setMinScale(value);
                break;
            // BORDER PARAMETERS
            case BORDERENABLE: {
                boolean enabled = Boolean.parseBoolean(value);
                if (enabled) {
                    lineSymbolizerWrapper.setHasStroke(true);
                }
                break;
            }
            case BORDERWIDTH: {
                lineSymbolizerWrapper.setStrokeWidth(value, fromField);
                break;
            }
            case BORDERCOLOR: {
                lineSymbolizerWrapper.setStrokeColor(value, fromField);
                break;
            }
            case BORDEROPACITY: {
                lineSymbolizerWrapper.setStrokeOpacity(value, fromField);
                break;
            }
            case GRAPHICSPATHBORDER: {
                if (values.length < 3) {
                    break;
                }
                String url = values[0];
                String width = values[1];
                String size = values[2];

                try {
                    if (url.length() == 0) {
                        //clear the graphics
                        lineSymbolizerWrapper.clearGraphicStroke();
                    } else {
                        lineSymbolizerWrapper.setStrokeExternalGraphicStrokePath(url);
                        Graphic graphicStroke = lineSymbolizerWrapper.getStrokeGraphicStroke();
                        graphicStroke.setSize(StyleUtil.ff.literal(size));
                        graphicStroke.setGap(StyleUtil.ff.literal(width));
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    throw new ErrorException("图片边框样式设置失败！");
                }
                break;
            }
            case DASH: {
                lineSymbolizerWrapper.setDash(value);
                break;
            }
            case DASHOFFSET: {
                lineSymbolizerWrapper.setDashOffset(value);
                break;
            }
            case LINECAP: {
                lineSymbolizerWrapper.setLineCap(value);
                break;
            }
            case LINEJOIN: {
                lineSymbolizerWrapper.setLineJoin(value);
                break;
            }
            case LINEEND: {
                lineSymbolizerWrapper.setEndPointStyle(values[0], values[1], values[2], values[3]);
                break;
            }
            case LINESTART: {
                lineSymbolizerWrapper.setStartPointStyle(values[0], values[1], values[2], values[3]);
                break;
            }
            // LABEL PARAMETERS
            case LABELENABLE: {
                boolean doEnable = Boolean.parseBoolean(value);
                if (doEnable) {
                    if (textSymbolizerWrapper == null) {
                        TextSymbolizer textSymbolizer = StyleUtil.createDefaultTextSymbolizer("LINE");
                        ruleWrapper.addSymbolizer(textSymbolizer, TextSymbolizerWrapper.class);
                    }
                } else {
                    ruleWrapper.removeTextSymbolizersWrapper();
                }
                break;
            }
            case LABEL: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setLabelName(value, fromField);
                break;
            }
            case LABELFONT: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                String name = values[0];
                String style = values[1];
                double height = Double.parseDouble(values[2]);
                Font font = StyleUtil.sb.createFont(name, style.equals("italic"), style.equals("normal"), height);

                textSymbolizerWrapper.setFont(font);
                break;
            }
            case LABELCOLOR: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setColor(value);
                break;
            }
            case LABELHALOCOLOR: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setHaloColor(value);
                break;
            }
            case LABELHALORADIUS: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setHaloRadius(value);
                break;
            }
            case LABELINITIALGAP: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setInitialGap(value);
                break;
            }
            case LABELPERPENDICULAROFFSET: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setPerpendicularOffset(value);
                break;
            }
            case LABELROTATION: {
                /*if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setRotation(value, fromField);*/
                break;
            }
            case LABELMAXDISPLACEMENT_VO: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setMaxDisplacementVO(value);
                break;
            }
            case LABELREPEAT_VO: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setRepeatVO(value);
                break;
            }
            case LABELAUTOWRAP_VO: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setAutoWrapVO(value);
                break;
            }
            case LABELSPACEAROUND_VO: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setSpaceAroundVO(value);
                break;
            }
            case LABELFOLLOWLINE_VO: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setFollowLineVO(value);
                break;
            }
            case LABELMAXANGLEDELTA_VO: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setMaxAngleDeltaVO(value);
                break;
            }
            case FILTER: {
                if (value.length() > 0) {
                    try {
                        Filter filter = ECQL.toFilter(value);
                        ruleWrapper.getRule().setFilter(filter);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ErrorException("样式过滤设置失败！");
                    }
                }
                break;
            }

            default:
                break;
        }
    }

    public void onStyleChangedByPolygon(String[] values, boolean fromField, StyleEventType styleEventType) throws ErrorException {
        String value = "";
        if (values.length > 0) {
            value = values[0];
        }

        PolygonSymbolizerWrapper polygonSymbolizerWrapper = ruleWrapper.getGeometrySymbolizersWrapper().adapt(
                PolygonSymbolizerWrapper.class);

        TextSymbolizerWrapper textSymbolizerWrapper = ruleWrapper.getTextSymbolizersWrapper();

        switch (styleEventType) {
            // GENERAL PARAMETERS
            case NAME:
                ruleWrapper.setName(value);
                break;
            case OFFSET:
                polygonSymbolizerWrapper.setOffset(values[0] + "," + values[1] + "," + values[2]);
                break;
            case MAXSCALE:
                ruleWrapper.setMaxScale(value);
                break;
            case MINSCALE:
                ruleWrapper.setMinScale(value);
                break;
            // BORDER PARAMETERS
            // BORDER PARAMETERS
            case BORDERENABLE: {
                boolean enabled = Boolean.parseBoolean(value);
                if (enabled) {
                    polygonSymbolizerWrapper.setHasStroke(true);
                }
                break;
            }
            case BORDERWIDTH: {
                polygonSymbolizerWrapper.setStrokeWidth(value, fromField);
                break;
            }
            case BORDERCOLOR: {
                polygonSymbolizerWrapper.setStrokeColor(value, fromField);
                break;
            }
            case BORDEROPACITY: {
                polygonSymbolizerWrapper.setStrokeOpacity(value, fromField);
                break;
            }
            case GRAPHICSPATHBORDER: {
                if (values.length < 3) {
                    break;
                }
                String url = values[0];
                String width = values[1];
                String size = values[2];

                try {
                    if (url.equals("")) { //$NON-NLS-1$
                        polygonSymbolizerWrapper.clearGraphicStroke();
                    } else {
                        polygonSymbolizerWrapper.setStrokeExternalGraphicStrokePath(url);
                        Graphic graphicStroke = polygonSymbolizerWrapper.getStrokeGraphicStroke();
                        graphicStroke.setSize(StyleUtil.ff.literal(size));
                        graphicStroke.setGap(StyleUtil.ff.literal(width));
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    throw new ErrorException("图片边框样式设置失败！");
                }
                break;
            }
            case DASH: {
                polygonSymbolizerWrapper.setDash(value);
                break;
            }
            case DASHOFFSET: {
                polygonSymbolizerWrapper.setDashOffset(value);
                break;
            }
            case LINECAP: {
                polygonSymbolizerWrapper.setLineCap(value);
                break;
            }
            case LINEJOIN: {
                polygonSymbolizerWrapper.setLineJoin(value);
                break;
            }
            case LINEEND: {
                polygonSymbolizerWrapper.setEndPointStyle(values[0], values[1], values[2], values[3]);
                break;
            }
            case LINESTART: {
                polygonSymbolizerWrapper.setStartPointStyle(values[0], values[1], values[2], values[3]);
                break;
            }
            // FILL PARAMETERS
            case FILLENABLE: {
                boolean enabled = Boolean.parseBoolean(value);
                if (enabled) {
                    polygonSymbolizerWrapper.setHasFill(true);
                }
                break;
            }
            case FILLCOLOR: {
                polygonSymbolizerWrapper.setFillColor(value, fromField);
                break;
            }
            case FILLOPACITY: {
                polygonSymbolizerWrapper.setFillOpacity(value, fromField);
                break;
            }
            case WKMGRAPHICSFILL: {
                if (values.length < 4) {
                    break;
                }
                if(values[0]==null || values[0].equals("")){
                    break;
                }
                polygonSymbolizerWrapper.clearGraphics();
                String wkmname = values[0];
                String wkmwidth = values[1];
                String wkmcolor = values[2];
                String wkmsize = values[3];
                polygonSymbolizerWrapper.setWkMarkNameFill(wkmname);
                polygonSymbolizerWrapper.setWkMarkWidthFill(wkmwidth);
                polygonSymbolizerWrapper.setWkMarkColorFill(wkmcolor);
                polygonSymbolizerWrapper.setWkMarkSizeFill(wkmsize);
                break;
            }
            case GRAPHICSPATHFILL: {
                try {
                    if (values.length < 2) {
                        break;
                    }
                    if (values[0]==null || values[0].equals("")) {
                        break;
                    }
                    polygonSymbolizerWrapper.setFillExternalGraphicFillPath(values[0], Double.parseDouble(values[1]));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    throw new ErrorException("图片填充样式设置失败！");
                }
                break;
            }
            // LABEL PARAMETERS
            case LABELENABLE: {
                boolean doEnable = Boolean.parseBoolean(value);
                if (doEnable) {
                    if (textSymbolizerWrapper == null) {
                        TextSymbolizer textSymbolizer = StyleUtil.createDefaultTextSymbolizer("POLYGON");
                        ruleWrapper.addSymbolizer(textSymbolizer, TextSymbolizerWrapper.class);
                    }
                } else {
                    ruleWrapper.removeTextSymbolizersWrapper();
                }
                break;
            }
            case LABEL: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setLabelName(value, fromField);
                break;
            }
            case LABELFONT: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                String name = values[0];
                String style = values[1];
                double height = Double.parseDouble(values[2]);
                Font font = StyleUtil.sb.createFont(name, style.equals("italic"), style.equals("normal"), height);

                textSymbolizerWrapper.setFont(font);
                break;
            }
            case LABELCOLOR: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setColor(value);
                break;
            }
            case LABELHALOCOLOR: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setHaloColor(value);
                break;
            }
            case LABELHALORADIUS: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setHaloRadius(value);
                break;
            }
            case LABELROTATION: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setRotation(value, fromField);
                break;
            }
            case LABELMAXDISPLACEMENT_VO: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setMaxDisplacementVO(value);
                break;
            }
            case LABELAUTOWRAP_VO: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setAutoWrapVO(value);
                break;
            }
            case LABELSPACEAROUND_VO: {
                if (textSymbolizerWrapper == null) {
                    break;
                }
                textSymbolizerWrapper.setSpaceAroundVO(value);
                break;
            }
            case FILTER: {
                if (value.length() > 0) {
                    try {
                        Filter filter = ECQL.toFilter(value);
                        ruleWrapper.getRule().setFilter(filter);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ErrorException("样式过滤设置失败！");
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    public void changStyle(MapStyle mapStyle) throws ErrorException {
        for (Field field : mapStyle.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(mapStyle) != null) {
                    switch (StyleEventType.getStyleEventType(field.getName())) {
                        case LABEL:
                            StyleVariable sv = (StyleVariable) field.get(mapStyle);
                            onStyleChanged(sv.getValues(), sv.getFromField(), StyleEventType.getStyleEventType(field.getName()));
                            break;
                        case OFFSET:
                        case GRAPHICSPATHBORDER:
                        case GRAPHICSPATHFILL:
                        case WKMGRAPHICSFILL:
                        case LINESTART:
                        case LINEEND:
                        case LABELFONT:
                        case LABELANCHOR:
                        case LABELDISPLACEMENT:
                            onStyleChanged((String[]) field.get(mapStyle), false, StyleEventType.getStyleEventType(field.getName()));
                            break;
                        default:
                            String values = field.get(mapStyle) + "";
                            if (values.equals("")) {
                                break;
                            }
                            onStyleChanged(new String[]{values}, false, StyleEventType.getStyleEventType(field.getName()));
                            break;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
