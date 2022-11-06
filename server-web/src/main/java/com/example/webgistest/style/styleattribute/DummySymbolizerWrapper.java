package com.example.webgistest.style.styleattribute;

import com.example.webgistest.style.utils.StyleUtil;
import org.geotools.styling.*;
import org.opengis.filter.expression.Expression;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * A wrapper for a {@link PointSymbolizer} to ease interaction with gui.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class DummySymbolizerWrapper extends SymbolizerWrapper {

    private String size;
    private String rotation;

    private String markName;
    private String fillColor;
    private String fillOpacity;
    private String strokeColor;
    private String strokeOpacity;
    private String strokeWidth;

    private boolean hasFill;
    private boolean hasStroke;
    private Mark mark;
    private Fill fill;
    private Stroke stroke;

    private final Graphic graphic;

    public DummySymbolizerWrapper(Symbolizer symbolizer, RuleWrapper parent) {
        super(symbolizer, parent);

        PointSymbolizer pointSymbolizer = (PointSymbolizer) symbolizer;
        graphic = pointSymbolizer.getGraphic();
        List<ExternalGraphic> externalGraphicsList = StyleUtil.externalGraphicsFromGraphic(graphic);

        // size
        Expression sizeExpr = graphic.getSize();
        String tmp = expressionToString(sizeExpr);
        if (tmp != null) {
            size = tmp;
        } else {
            size = StyleUtil.DEFAULT_WIDTH;
        }
        // rotation
        Expression rotationExpr = graphic.getRotation();
        tmp = expressionToString(rotationExpr);
        if (tmp != null) {
            rotation = tmp;
        } else {
            rotation = StyleUtil.DEFAULT_ROTATION;
        }
        // offset
        Point2D offset = StyleUtil.getOffset(pointSymbolizer);
        if (offset != null) {
            xOffset = String.valueOf(offset.getX());
            yOffset = String.valueOf(offset.getY());
        } else {
            xOffset = StyleUtil.DEFAULT_OFFSET;
            yOffset = StyleUtil.DEFAULT_OFFSET;
        }

        if (externalGraphicsList.size() == 0) {
            mark = SLD.mark(pointSymbolizer);
            if (mark == null) {
                return;
            }
            markName = mark.getWellKnownName().evaluate(null, String.class);
            if (markName == null || markName.equals("")) { //$NON-NLS-1$
                markName = "circle"; //$NON-NLS-1$
                mark.setWellKnownName(StyleUtil.ff.literal(markName));
            }

            fill = mark.getFill();
            if (fill != null) {
                fillColor = fill.getColor().evaluate(null, String.class);
                Expression opacityExpr = fill.getOpacity();
                fillOpacity = expressionToString(opacityExpr);
                hasFill = true;
            } else {
                hasFill = false;
            }

            stroke = mark.getStroke();
            if (stroke != null) {
                Expression color = stroke.getColor();
                tmp = color.evaluate(null, String.class);
                if (tmp != null) {
                    strokeColor = tmp;
                } else {
                    strokeColor = StyleUtil.DEFAULT_COLOR;
                }

                Expression opacity = stroke.getOpacity();
                tmp = expressionToString(opacity);
                if (tmp != null) {
                    strokeOpacity = tmp;
                } else {
                    strokeOpacity = StyleUtil.DEFAULT_OPACITY;
                }

                Expression width = stroke.getWidth();
                tmp = expressionToString(width);
                if (tmp != null) {
                    strokeWidth = tmp;
                } else {
                    strokeWidth = StyleUtil.DEFAULT_WIDTH;
                }
                hasStroke = true;
            } else {
                hasStroke = false;
            }
        } else {
            // graphics case
            externalGraphic = externalGraphicsList.get(0);
        }
    }

    public <T> T adapt(Class<T> adaptee) {
        if (adaptee.isAssignableFrom(PointSymbolizerWrapper.class)) {
            PointSymbolizerWrapper geometrySymbolizersWrapper = new PointSymbolizerWrapper(
                    StyleUtil.createDefaultGeometrySymbolizer("POINT"), null);
            return adaptee.cast(geometrySymbolizersWrapper);
        } else if (adaptee.isAssignableFrom(LineSymbolizerWrapper.class)) {
            LineSymbolizerWrapper geometrySymbolizersWrapper = new LineSymbolizerWrapper(
                    StyleUtil.createDefaultGeometrySymbolizer("LINE"), null);
            return adaptee.cast(geometrySymbolizersWrapper);
        } else if (adaptee.isAssignableFrom(PolygonSymbolizerWrapper.class)) {
            PolygonSymbolizerWrapper geometrySymbolizersWrapper = new PolygonSymbolizerWrapper(
                    StyleUtil.createDefaultGeometrySymbolizer("POLYGON"), null);
            return adaptee.cast(geometrySymbolizersWrapper);
        } else if (adaptee.isAssignableFrom(TextSymbolizerWrapper.class)) {
            return adaptee.cast(this);
        }
        return null;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    // ///// GETTERS/SETTERS
    public void setSize(String size, boolean isProperty) {
        this.size = size;
        if (isProperty) {
            graphic.setSize(StyleUtil.ff.property(size));
        } else {
            graphic.setSize(StyleUtil.ff.literal(size));
        }
    }

    public void setRotation(String rotation, boolean isProperty) {
        this.rotation = rotation;
        if (isProperty) {
            graphic.setRotation(StyleUtil.ff.property(rotation));
        } else {
            graphic.setRotation(StyleUtil.ff.literal(rotation));
        }
    }

    public void setMarkName(String markName) {
        if (markName == null || markName.equals("")) { //$NON-NLS-1$
            return;
        }
        this.markName = markName;
        if (mark == null) {
            mark = StyleUtil.sf.createMark();
        }
        mark.setWellKnownName(StyleUtil.ff.literal(markName));

        graphic.graphicalSymbols().clear();
        graphic.graphicalSymbols().add(mark);
    }

    public void setFillColor(String fillColor) {
        if (fillColor == null) {
            hasFill = false;
        } else {
            hasFill = true;
        }
        this.fillColor = fillColor;
        checkFillExists();
        fill.setColor(StyleUtil.ff.literal(fillColor));
    }

    public void setFillOpacity(String fillOpacity, boolean isProperty) {
        this.fillOpacity = fillOpacity;
        checkFillExists();
        if (isProperty) {
            fill.setOpacity(StyleUtil.ff.property(fillOpacity));
        } else {
            fill.setOpacity(StyleUtil.ff.literal(fillOpacity));
        }
    }

    public void setHasStroke(boolean hasStroke) {
        this.hasStroke = hasStroke;
        if (hasStroke) {
            checkStrokeExists();
        } else {
            stroke = null;
            mark.setStroke(null);
        }
    }

    public void setHasFill(boolean hasFill) {
        this.hasFill = hasFill;
        if (hasFill) {
            checkFillExists();
        } else {
            fill = null;
            mark.setFill(null);
        }
    }

    private void checkStrokeExists() {
        if (stroke == null) {
            stroke = StyleUtil.sf.createStroke(StyleUtil.ff.literal(strokeColor), StyleUtil.ff.literal(strokeWidth));
            if (mark != null) {
                mark.setStroke(stroke);
            }
        }
    }

    private void checkFillExists() {
        if (fill == null) {
            fill = StyleUtil.sf.createFill(StyleUtil.ff.literal(fillColor));
            if (mark != null) {
                mark.setFill(fill);
            }
        }
    }

    public void setStrokeWidth(String strokeWidth, boolean isProperty) {
        this.strokeWidth = strokeWidth;
        checkStrokeExists();
        if (isProperty) {
            stroke.setWidth(StyleUtil.ff.property(strokeWidth));
        } else {
            stroke.setWidth(StyleUtil.ff.literal(strokeWidth));
        }
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
        checkStrokeExists();
        if (strokeColor == null) {
            hasStroke = false;
        } else {
            hasStroke = true;
        }
        stroke.setColor(StyleUtil.ff.literal(strokeColor));
    }

    public void setStrokeOpacity(String strokeOpacity, boolean isProperty) {
        this.strokeOpacity = strokeOpacity;
        checkStrokeExists();
        if (isProperty) {
            stroke.setOpacity(StyleUtil.ff.property(strokeOpacity));
        } else {
            stroke.setOpacity(StyleUtil.ff.literal(strokeOpacity));
        }
    }

    // getters
    public String getSize() {
        return size;
    }

    public String getRotation() {
        return rotation;
    }

    public String getMarkName() {
        return markName;
    }

    public String getFillColor() {
        return fillColor;
    }

    public String getFillOpacity() {
        return fillOpacity;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public String getStrokeOpacity() {
        return strokeOpacity;
    }

    public String getStrokeWidth() {
        return strokeWidth;
    }

    public boolean hasFill() {
        return hasFill;
    }

    public boolean hasStroke() {
        return hasStroke;
    }
}

