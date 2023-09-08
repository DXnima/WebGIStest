package com.example.webgistest.style.styleattribute;

import com.example.webgistest.style.utils.StyleUtil;
import org.geotools.styling.*;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * A wrapper for a {@link PolygonSymbolizer} to ease interaction with gui.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class PolygonSymbolizerWrapper extends LineSymbolizerWrapper {

    private Fill fill;
    private String fillColor;
    private String fillOpacity;
    private Graphic fillGraphicFill;
    private String wkMarkNameFill;
    private String wkMarkColorFill;
    private String wkMarkWidthFill;
    private String wkMarkSizeFill;
    private boolean hasFill;
    private Mark mark;

    public PolygonSymbolizerWrapper(Symbolizer tmpSymbolizer, RuleWrapper parent) {
        super((PolygonSymbolizer) tmpSymbolizer, parent);

        PolygonSymbolizer polygonSymbolizer = (PolygonSymbolizer) tmpSymbolizer;

        // offset
        Point2D offset = StyleUtil.getOffset(polygonSymbolizer);
        if (offset != null) {
            xOffset = String.valueOf(offset.getX());
            yOffset = String.valueOf(offset.getY());
        } else {
            xOffset = StyleUtil.DEFAULT_OFFSET;
            yOffset = StyleUtil.DEFAULT_OFFSET;
        }

        stroke = polygonSymbolizer.getStroke();
        if (stroke != null) {
            Expression color = stroke.getColor();
            strokeColor = expressionToString(color);
            Expression width = stroke.getWidth();
            strokeWidth = expressionToString(width);
            Expression opacity = stroke.getOpacity();
            strokeOpacity = expressionToString(opacity);

            if (strokeColor == null) {
                strokeColor = StyleUtil.DEFAULT_COLOR;
                stroke.setColor(StyleUtil.ff.literal(StyleUtil.DEFAULT_COLOR));
            }
            if (strokeOpacity == null) {
                strokeOpacity = StyleUtil.DEFAULT_OPACITY;
                stroke.setOpacity(StyleUtil.ff.literal(StyleUtil.DEFAULT_OPACITY));
            }
            if (strokeWidth == null) {
                strokeWidth = StyleUtil.DEFAULT_WIDTH;
                stroke.setWidth(StyleUtil.ff.literal(StyleUtil.DEFAULT_WIDTH));
            }

            strokeGraphicStroke = stroke.getGraphicStroke();
            if (strokeGraphicStroke != null) {
                List<GraphicalSymbol> graphicalSymbolsList = strokeGraphicStroke.graphicalSymbols();
                if (graphicalSymbolsList.size() > 0) {
                    GraphicalSymbol graphicalSymbol = graphicalSymbolsList.get(0);
                    if (graphicalSymbol instanceof ExternalGraphic) {
                        strokeExternalGraphicStroke = (ExternalGraphic) graphicalSymbol;
                    }
                }
            }

            // dash
            float[] dashArray = stroke.getDashArray();
            if (dashArray != null) {
                dash = StyleUtil.getDashString(dashArray);
            } else {
                dash = ""; //$NON-NLS-1$
            }
            // dashoffset
            dashOffset = stroke.getDashOffset().evaluate(null, String.class);
            // line cap
            lineCap = stroke.getLineCap().evaluate(null, String.class);
            // line join
            lineJoin = stroke.getLineJoin().evaluate(null, String.class);

            hasStroke = true;
        } else {
            hasStroke = false;
        }

        fill = polygonSymbolizer.getFill();
        if (fill != null) {
            Expression color = fill.getColor();
            if (color != null) {
                fillColor = expressionToString(color);
            }
            Expression opacity = fill.getOpacity();
            fillOpacity = expressionToString(opacity);

            fillGraphicFill = fill.getGraphicFill();
            if (fillGraphicFill != null) {
                List<GraphicalSymbol> graphicalSymbolsList = fillGraphicFill.graphicalSymbols();

                if (graphicalSymbolsList.size() > 0) {
                    GraphicalSymbol graphicalSymbol = graphicalSymbolsList.get(0);
                    if (graphicalSymbol instanceof ExternalGraphic) {
                        fillExternalGraphicFill = (ExternalGraphic) graphicalSymbol;
                    } else if (graphicalSymbol instanceof Mark) {
                        mark = (Mark) graphicalSymbol;
                        wkMarkNameFill = mark.getWellKnownName().evaluate(null, String.class);
                        Stroke stroke = mark.getStroke();
                        wkMarkColorFill = stroke.getColor().evaluate(null, String.class);
                        wkMarkWidthFill = stroke.getWidth().evaluate(null, String.class);
                        wkMarkSizeFill = fillGraphicFill.getSize().evaluate(null, String.class);
                    }
                }
            }

            hasFill = true;
        } else {
            hasFill = false;
        }

    }

    private void checkFillExists() {
        if (fill == null) {
            if (fillColor == null) {
                fillColor = StyleUtil.DEFAULT_COLOR;
            }
            fill = StyleUtil.sf.createFill(StyleUtil.ff.literal(fillColor));
            PolygonSymbolizer polygonSymbolizer = (PolygonSymbolizer) getSymbolizer();
            polygonSymbolizer.setFill(fill);
            fillGraphicFill = fill.getGraphicFill();
        }
    }

    private void checkMarkExists() {
        if (mark == null) {
            mark = StyleUtil.sf.createMark();
            checkFillExists();
            if (fillGraphicFill == null) {
                fillGraphicFill = StyleUtil.sf.createDefaultGraphic();
                fill.setGraphicFill(fillGraphicFill);
            }
            fillGraphicFill.graphicalSymbols().clear();
            fillGraphicFill.graphicalSymbols().add(mark);
        }
    }

    public Graphic getFillGraphicFill() {
        return fillGraphicFill;
    }


    public void setFillGraphicFill(Graphic fillGraphicFill) {
        this.fillGraphicFill = fillGraphicFill;
        checkFillExists();

        fill.setGraphicFill(fillGraphicFill);
    }

    // ///// GETTERS/SETTERS
    public void setHasFill(boolean hasFill) {
        this.hasFill = hasFill;
        if (hasFill) {
            checkFillExists();
        } else {
            fill = null;
            PolygonSymbolizer polygonSymbolizer = (PolygonSymbolizer) getSymbolizer();
            polygonSymbolizer.setFill(null);
        }
    }

    public void setHasStroke(boolean hasStroke) {
        this.hasStroke = hasStroke;
        if (hasStroke) {
            checkStrokeExists();
        } else {
            stroke = null;
            PolygonSymbolizer polygonSymbolizer = (PolygonSymbolizer) getSymbolizer();
            polygonSymbolizer.setStroke(null);
        }
    }

    protected void checkStrokeExists() {
        if (stroke == null) {
            if (strokeColor == null) {
                strokeColor = StyleUtil.DEFAULT_COLOR;
            }
            if (strokeWidth == null) {
                strokeWidth = StyleUtil.DEFAULT_WIDTH;
            }
            stroke = StyleUtil.sf.createStroke(StyleUtil.ff.literal(strokeColor), StyleUtil.ff.literal(strokeWidth));
            PolygonSymbolizer polygonSymbolizer = (PolygonSymbolizer) getSymbolizer();
            polygonSymbolizer.setStroke(stroke);
            strokeGraphicStroke = stroke.getGraphicStroke();
        }
    }

    public void setFillColor(String fillColor, boolean isProperty) {
        this.fillColor = fillColor;
        checkFillExists();
        fill.setGraphicFill(null);
        fillGraphicFill = null;
        if (isProperty) {
            fill.setColor(StyleUtil.ff.property(fillColor));
        } else {
            if (fillColor == null) {
                hasFill = false;
            } else {
                hasFill = true;
            }
            fill.setColor(StyleUtil.ff.literal(fillColor));
        }
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

    public void clearGraphics() {
        this.wkMarkColorFill = null;
        this.wkMarkNameFill = null;
        this.wkMarkSizeFill = null;
        this.wkMarkWidthFill = null;

        if (fillGraphicFill != null) {
            fillGraphicFill.graphicalSymbols().clear();
        }
//    	fillGraphicFill = null;
        mark = null;
    }

    public void setWkMarkNameFill(String wkMarkNameFill) {
        this.wkMarkNameFill = wkMarkNameFill;
        checkMarkExists();
        if (wkMarkNameFill != null) {
            mark.setWellKnownName(StyleUtil.ff.literal(wkMarkNameFill));
        }
    }

    public void setWkMarkColorFill(String wkMarkColorFill) {
        this.wkMarkColorFill = wkMarkColorFill;
        checkMarkExists();
        if (wkMarkColorFill != null) {
            Stroke markStroke = mark.getStroke();
            markStroke.setColor(StyleUtil.ff.literal(wkMarkColorFill));
        }
    }

    public void setWkMarkWidthFill(String wkMarkWidthFill) {
        this.wkMarkWidthFill = wkMarkWidthFill;
        checkMarkExists();
        if (wkMarkWidthFill != null) {
            Stroke markStroke = mark.getStroke();
            markStroke.setWidth(StyleUtil.ff.literal(wkMarkWidthFill));
        }
    }

    public void setWkMarkSizeFill(String wkMarkSizeFill) {
        this.wkMarkSizeFill = wkMarkSizeFill;
        checkFillExists();
        fillGraphicFill.setSize(StyleUtil.ff.literal(wkMarkSizeFill));
    }

    // getters
    public boolean hasFill() {
        return hasFill;
    }

    public String getFillColor() {
        return fillColor;
    }

    public String getFillOpacity() {
        return fillOpacity;
    }

    public String getWkMarkNameFill() {
        return wkMarkNameFill;
    }

    public String getWkMarkColorFill() {
        return wkMarkColorFill;
    }

    public String getWkMarkWidthFill() {
        return wkMarkWidthFill;
    }

    public String getWkMarkSizeFill() {
        return wkMarkSizeFill;
    }

}
