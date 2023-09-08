package com.example.webgistest.style.utils;

import com.example.webgistest.exception.ErrorException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.Filters;
import org.geotools.styling.Font;
import org.geotools.styling.Stroke;
import org.geotools.styling.*;
import org.geotools.xml.styling.SLDParser;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.*;
import org.opengis.style.Graphic;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.SemanticType;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Utility class for working with Geotools SLD objects.
 * <p>
 * This class assumes a subset of the SLD specification:
 * <ul>
 * <li>Single Rule - matching Filter.INCLUDE
 * <li>Symbolizer lookup by name
 * </ul>
 * </p>
 * <p>
 * When you start to branch out to SLD information that contains
 * multiple rules you will need to modify this class.
 * </p>
 *
 * @author Jody Garnett, Refractions Research.
 * @version 1.3.2
 * @since 0.7.0
 */
public class SLDUtil extends SLD {
    private static StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    public static final double ALIGN_LEFT = 1.0;
    public static final double ALIGN_CENTER = 0.5;
    public static final double ALIGN_RIGHT = 0.0;
    public static final double ALIGN_BOTTOM = 1.0;
    public static final double ALIGN_MIDDLE = 0.5;
    public static final double ALIGN_TOP = 0.0;

    public static int size(Graphic graphic) {
        if (graphic == null) {
            return NOTFOUND;
        }
        return Filters.asInt(graphic.getSize());
    }

    public static Color polyFill(PolygonSymbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Fill fill = symbolizer.getFill();

        if (fill == null) {
            return null;
        }

        Expression color = fill.getColor();
        return color(color);
    }

    public static Color color(Expression expr) {
        if (expr == null) {
            return null;
        }
        try {
            return expr.evaluate(null, Color.class);
        } catch (Throwable t) {
            class ColorVisitor implements ExpressionVisitor {
                Color found;

                public Object visit(Literal expr, Object data) {
                    if (found != null) return null;
                    try {
                        Color color = expr.evaluate(expr, Color.class);
                        if (color != null) {
                            found = color;
                        }
                    } catch (Throwable t) {
                        // not a color
                    }
                    return data;
                }

                public Object visit(NilExpression arg0, Object data) {
                    return data;
                }

                public Object visit(Add arg0, Object data) {
                    return data;
                }

                public Object visit(Divide arg0, Object data) {
                    return null;
                }

                public Object visit(Function function, Object data) {
                    for (Expression param : function.getParameters()) {
                        param.accept(this, data);
                    }
                    return data;
                }

                public Object visit(Multiply arg0, Object data) {
                    return data;
                }

                public Object visit(PropertyName arg0, Object data) {
                    return data;
                }

                public Object visit(Subtract arg0, Object data) {
                    return data;
                }
            }
            ColorVisitor search = new ColorVisitor();
            expr.accept(search, null);

            return search.found;
        }
    }

    /**
     * Retrieves all colour names defined in a rule
     *
     * @param rule the rule
     * @return an array of unique colour names
     */
    public static String[] colors(Rule rule) {
        Set<String> colorSet = new HashSet<String>();

        Color color = null;
        for (Symbolizer sym : rule.symbolizers()) {
            if (sym instanceof PolygonSymbolizer) {
                PolygonSymbolizer symb = (PolygonSymbolizer) sym;
                color = polyFill(symb);

            } else if (sym instanceof LineSymbolizer) {
                LineSymbolizer symb = (LineSymbolizer) sym;
                color = color(symb);

            } else if (sym instanceof PointSymbolizer) {
                PointSymbolizer symb = (PointSymbolizer) sym;
                color = pointFillWithAlpha(symb);
            }

            if (color != null) {
                colorSet.add(SLD.colorToHex(color));
            }
        }

        if (colorSet.size() > 0) {
            return colorSet.toArray(new String[0]);
        } else {
            return new String[0];
        }
    }

    /**
     * Extracts the fill color with a given opacity from the {@link PointSymbolizer}.
     *
     * @param symbolizer the point symbolizer from which to get the color.
     * @return the {@link Color} with transparency if available. Returns null if no color is available.
     */
    public static Color pointFillWithAlpha(PointSymbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Graphic graphic = symbolizer.getGraphic();
        if (graphic == null) {
            return null;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if ((gs != null) && (gs instanceof Mark)) {
                Mark mark = (Mark) gs;
                Fill fill = mark.getFill();
                if (fill != null) {
                    Color colour = color(fill.getColor());
                    if (colour == null) {
                        return null;
                    }
                    Expression opacity = fill.getOpacity();
                    if (opacity == null)
                        opacity = ff.literal(1.0);
                    float alpha = (float) Filters.asDouble(opacity);
                    colour = new Color(colour.getRed() / 255f, colour.getGreen() / 255f, colour.getBlue() / 255f, alpha);
                    if (colour != null) {
                        return colour;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Extracts the stroke color with a given opacity from the {@link PointSymbolizer}.
     *
     * @param symbolizer the point symbolizer from which to get the color.
     * @return the {@link Color} with transparency if available. Returns null if no color is available.
     */
    public static Color pointStrokeColorWithAlpha(PointSymbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Graphic graphic = symbolizer.getGraphic();
        if (graphic == null) {
            return null;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if ((gs != null) && (gs instanceof Mark)) {
                Mark mark = (Mark) gs;
                Stroke stroke = mark.getStroke();
                if (stroke != null) {
                    Color colour = color(stroke);
                    if (colour == null) {
                        return null;
                    }
                    Expression opacity = stroke.getOpacity();
                    if (opacity == null)
                        opacity = ff.literal(1.0);
                    float alpha = (float) Filters.asDouble(opacity);
                    colour = new Color(colour.getRed() / 255f, colour.getGreen() / 255f, colour.getBlue() / 255f, alpha);
                    if (colour != null) {
                        return colour;
                    }
                }
            }
        }

        return null;
    }

    public static Font font(TextSymbolizer symbolizer) {
        if (symbolizer == null)
            return null;
        Font font = symbolizer.getFont();
        return font;
    }

    public static Style getDefaultStyle(StyledLayerDescriptor sld) {
        Style[] styles = styles(sld);
        for (int i = 0; i < styles.length; i++) {
            Style style = styles[i];
            List<FeatureTypeStyle> ftStyles = style.featureTypeStyles();
            genericizeftStyles(ftStyles);
            if (style.isDefault()) {
                return style;
            }
        }
        // no default, so just grab the first one
        return styles[0];
    }

    /**
     * Converts the type name of all FeatureTypeStyles to Feature so that the all apply to any feature type.  This is admittedly dangerous
     * but is extremely useful because it means that the style can be used with any feature type.
     *
     * @param ftStyles
     */
    private static void genericizeftStyles(List<FeatureTypeStyle> ftStyles) {
        for (FeatureTypeStyle featureTypeStyle : ftStyles) {
            featureTypeStyle.featureTypeNames().clear();
            featureTypeStyle.featureTypeNames().add(new NameImpl(SLDUtil.GENERIC_FEATURE_TYPENAME));
        }
    }

    public static boolean isSemanticTypeMatch(FeatureTypeStyle fts, String regex) {
        for (SemanticType type : fts.semanticTypeIdentifiers()) {
            if (type.name().matches(regex)) return true;
        }
        return false;
    }

    /**
     * Returns the min scale of the default rule, or 0 if none is set
     */
    public static double minScale(FeatureTypeStyle fts) {
        if (fts == null || fts.rules().isEmpty()) {
            return 0.0;
        }
        Rule r = fts.rules().get(0);
        return r.getMinScaleDenominator();
    }

    /**
     * Returns the max scale of the default rule, or {@linkplain Double#NaN} if none is set
     */
    public static double maxScale(FeatureTypeStyle fts) {
        if (fts == null || fts.rules().isEmpty()) {
            return Double.NaN;
        }
        Rule r = fts.rules().get(0);
        return r.getMaxScaleDenominator();
    }

    /**
     * gets the first FeatureTypeStyle
     */
    public static FeatureTypeStyle getFeatureTypeStyle(Style s) {
        List<FeatureTypeStyle> fts = s.featureTypeStyles();
        if (!fts.isEmpty()) {
            return fts.get(0);
        }
        return null;
    }

    /**
     * Find the first rule which contains a rastersymbolizer, and return it
     *
     * @param s A style to search in
     * @return a rule, or null if no raster symbolizers are found.
     */
    public static Rule getRasterSymbolizerRule(Style s) {
        for (FeatureTypeStyle featureTypeStyle : s.featureTypeStyles()) {
            for (Rule rule : featureTypeStyle.rules()) {
                for (Symbolizer symbolizer : rule.getSymbolizers()) {
                    if (symbolizer instanceof RasterSymbolizer) {
                        return rule;
                    }
                }
            }
        }
        return null;
    }

    /**
     * The type name that can be used in an SLD in the featuretypestyle that matches all feature types.
     */
    public static final String GENERIC_FEATURE_TYPENAME = "Feature";


    /**
     * Creates an SLD and UserLayer, and nests the style (SLD-->UserLayer-->Style).
     *
     * @param style
     * @return SLD
     */
    public static StyledLayerDescriptor createDefaultSLD(Style style) {
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        UserLayer layer = sf.createUserLayer();
        //FeatureTypeConstraint ftc = styleFactory.createFeatureTypeConstraint(null, Filter.INCLUDE, null);
        layer.setLayerFeatureConstraints(new FeatureTypeConstraint[]{null});
        sld.addStyledLayer(layer);
        layer.addUserStyle(style);
        return sld;
    }

    public static StyledLayerDescriptor XMLtoSLD(String xml) {
        //save changes to style object
        StyleFactory factory = CommonFactoryFinder.getStyleFactory();
        String encoding = getEncoding(xml);
        InputStream is = getXMLasInputStream(xml, encoding);
        if (is == null) return null;
        SLDParser stylereader = new SLDParser(factory, is);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        return sld;
    }

    public static InputStream getXMLasInputStream(String xml, String encoding) {
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(xml.getBytes(encoding));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            throw new RuntimeException("XML格式不对！");
        }
        return is;
    }

    public static Style XMLtoStyle(String xml) throws ErrorException {
        if (xml == null || xml.equals("")) {
            throw new ErrorException("XML为空！");
        }
        StyledLayerDescriptor sld = XMLtoSLD(xml);
        return getDefaultStyle(sld);
    }

    private static String getEncoding(String xmlHead) {
        String result = null;
        String xml = xmlHead.trim();
        if (xml.startsWith("<?xml")) {
            int end = xml.indexOf("?>");
            String sub = xml.substring(0, end);
            StringTokenizer tokens = new StringTokenizer(sub, " =\"'");
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                if ("encoding".equals(token)) {
                    if (!(tokens.hasMoreTokens()))
                        break;
                    result = tokens.nextToken();
                    break;
                }
            }
        }
        return result;
    }

}
