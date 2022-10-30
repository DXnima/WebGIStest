package com.example.webgistest.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.FilterFunction_offset;
import org.geotools.styling.Font;
import org.geotools.styling.Stroke;
import org.geotools.styling.*;
import org.geotools.util.factory.GeoTools;
import org.geotools.xml.styling.SLDParser;
import org.geotools.xml.styling.SLDTransformer;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.style.GraphicalSymbol;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SLDUtil {
    public static final String NONE = "- none -";

    public static final String DEFAULT_SIZE = "5";
    public static final String DEFAULT_WIDTH = "1";
    public static final String DEFAULT_ROTATION = "0";
    public static final String DEFAULT_OFFSET = "0";
    public static final String DEFAULT_OPACITY = "1";
    public static final String DEFAULT_COLOR = "#000000";
    public static final String DEFAULT_MINSCALE = "0";
    public static final String DEFAULT_MAXSCALE = "infinity";

    // offset values
    public static final int OFFSET_MAX = 1000;
    public static final int OFFSET_MIN = -1000;
    public static final int OFFSET_STEP = 10;
    // displacement values
    public static final int DISPLACEMENT_MAX = 1000;
    public static final int DISPLACEMENT_MIN = -1000;
    public static final int DISPLACEMENT_STEP = 10;

    public static final String DEFAULT_GROUPNAME = "group ";
    public static final String DEFAULT_STYLENAME = "default style";

    public static final String SLD_EXTENTION = ".sld";

    /**
     * The SLD defined well known mark codes.
     */
    public static final String[] wkMarkDefs = {//
            "", //
            "cross", //
            "circle", //
            "triangle", //
            "X", //
            "star", //
            "arrow", //
            "hatch", //
            "square"//
    };

    /**
     * The SLD defined well known mark names for gui use.
     */
    public static final String[] wkMarkNames = {//
            "", //
            "cross", //
            "circle", //
            "triangle", //
            "X", //
            "star", //
            "arrow", //
            "hatch", //
            "square"//
    };

    public static final String SHAPE_PREFIX = "shape://";

    /**
     * The custom shape mark names as needed by geotools.
     */
    public static final String[] shapeMarkDefs = { //
            "", //
            SHAPE_PREFIX + "vertline", //
            SHAPE_PREFIX + "horline", //
            SHAPE_PREFIX + "slash", //
            SHAPE_PREFIX + "backslash", //
            SHAPE_PREFIX + "times", //
            SHAPE_PREFIX + "dot", //
            SHAPE_PREFIX + "plus" //
    };
    /**
     * The custom shape mark names for gui.
     */
    public static final String[] shapeMarkNames = { //
            "", //
            "vertical lines", //
            "horizontal lines", //
            "diagonal lines", //
            "inverse diagonal lines", //
            "crossed diagonal lines", //
            "dots", //
            "plus" //
    };

    /**
     * A map of names for all the marks.
     */
    public static final BiMap<String, String> markNamesToDef = HashBiMap.create();

    static {

        // well known marks
        markNamesToDef.put(wkMarkNames[0], wkMarkDefs[0]);
        markNamesToDef.put(wkMarkNames[1], wkMarkDefs[1]);
        markNamesToDef.put(wkMarkNames[2], wkMarkDefs[2]);
        markNamesToDef.put(wkMarkNames[3], wkMarkDefs[3]);
        markNamesToDef.put(wkMarkNames[4], wkMarkDefs[4]);
        markNamesToDef.put(wkMarkNames[5], wkMarkDefs[5]);
        markNamesToDef.put(wkMarkNames[6], wkMarkDefs[6]);
        markNamesToDef.put(wkMarkNames[7], wkMarkDefs[7]);
        markNamesToDef.put(wkMarkNames[8], wkMarkDefs[8]);
        // custom shapes
        markNamesToDef.put(shapeMarkNames[1], shapeMarkDefs[1]);
        markNamesToDef.put(shapeMarkNames[2], shapeMarkDefs[2]);
        markNamesToDef.put(shapeMarkNames[3], shapeMarkDefs[3]);
        markNamesToDef.put(shapeMarkNames[4], shapeMarkDefs[4]);
        markNamesToDef.put(shapeMarkNames[5], shapeMarkDefs[5]);
        markNamesToDef.put(shapeMarkNames[6], shapeMarkDefs[6]);
        markNamesToDef.put(shapeMarkNames[7], shapeMarkDefs[7]);
    }

    /**
     * Getter for an array of all available marks.
     *
     * @return all mark names (for gui use).
     */
    public static String[] getAllMarksArray() {
        Set<String> keySet = markNamesToDef.keySet();
        return (String[]) keySet.toArray(new String[keySet.size()]);
    }

    /**
     * The SLD names of the line cap definitions.
     */
    public static final String[] lineCapNames = { //
            "", //
            "butt", //
            "round", //
            "square" //
    };

    /**
     * The SLD names of the line join definitions.
     */
    public static final String[] verticalPlacementNames = { //
            "bevel", //
            "miter", //
            "round" //
    };

    /**
     * The SLD names of the line join definitions.
     */
    public static final String[] lineJoinNames = { //
            "", //
            "bevel", //
            "miter", //
            "round" //
    };

    /**
     * A map of user friendly names to the SLD names of line
     * end styles.
     */
    public static final BiMap<String, String> lineEndStyles = HashBiMap.create();

    static {
        lineEndStyles.put("arrow - open", "shape://oarrow");
        lineEndStyles.put("arrow - closed", "shape://carrow");
        lineEndStyles.put("circle", "circle");
        lineEndStyles.put("square", "square");
    }

    /**
     * The default {@link StyleFactory} to use.
     */
    public static StyleFactory sf = CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints());

    /**
     * The default {@link FilterFactory} to use.
     */
    public static FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());

    /**
     * The default {@link StyleBuilder} to use.
     */
    public static StyleBuilder sb = new StyleBuilder(sf, ff);

    /**
     * Parse a file and extract the {@link StyledLayerDescriptor}.
     *
     * @param file the sld file to parse.
     * @return the styled layer descriptor.
     * @throws IOException
     */
    public static StyledLayerDescriptor readStyle( File file ) throws IOException {
        SLDParser stylereader = new SLDParser(sf, file);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        return sld;
    }

    /**
     * Collect all {@link ExternalGraphic}s from the given {@link Rule}.
     *
     * @param rule the rule to check.
     * @return the extracted {@link ExternalGraphic}s.
     */
    public static List<ExternalGraphic> externalGraphicsFromRule( Rule rule ) {
        List<ExternalGraphic> gList = new ArrayList<ExternalGraphic>();
        List<Symbolizer> symbolizers = rule.symbolizers();
        if (symbolizers.size() != 0) {
            for( Symbolizer symbolizer : symbolizers ) {
                Graphic[] graphics = new Graphic[2];
                if (symbolizer instanceof PointSymbolizer) {
                    PointSymbolizer pointSymbolizer = (PointSymbolizer) symbolizer;
                    graphics[0] = pointSymbolizer.getGraphic();
                } else if (symbolizer instanceof LineSymbolizer) {
                    LineSymbolizer lineSymbolizer = (LineSymbolizer) symbolizer;
                    Stroke stroke = lineSymbolizer.getStroke();
                    graphics[0] = stroke.getGraphicStroke();
                } else if (symbolizer instanceof PolygonSymbolizer) {
                    PolygonSymbolizer polygonSymbolizer = (PolygonSymbolizer) symbolizer;
                    Stroke stroke = polygonSymbolizer.getStroke();
                    if (stroke != null)
                        graphics[0] = stroke.getGraphicStroke();
                    Fill fill = polygonSymbolizer.getFill();
                    if (fill != null)
                        graphics[1] = fill.getGraphicFill();
                }
                for( int i = 0; i < graphics.length; i++ ) {
                    if (graphics[i] != null) {
                        for( GraphicalSymbol gs : graphics[i].graphicalSymbols() ) {
                            if ((gs != null) && (gs instanceof ExternalGraphic)) {
                                ExternalGraphic externalGraphic = (ExternalGraphic) gs;
                                gList.add(externalGraphic);
                            }
                        }
                    }
                }
            }
            return gList;
        }
        return Collections.emptyList();
    }

    /**
     * Collect all {@link ExternalGraphic}s from the given {@link Graphic}.
     *
     * @param graphic the graphic to check.
     * @return the extracted {@link ExternalGraphic}s.
     */
    public static List<ExternalGraphic> externalGraphicsFromGraphic( Graphic graphic ) {
        List<ExternalGraphic> gList = new ArrayList<ExternalGraphic>();
        for( GraphicalSymbol gs : graphic.graphicalSymbols() ) {
            if ((gs != null) && (gs instanceof ExternalGraphic)) {
                ExternalGraphic externalGraphic = (ExternalGraphic) gs;
                gList.add(externalGraphic);
            }
        }
        return gList;
    }

    /**
     * Creates a default {@link Style} for a point.
     *
     * @return the default style.
     */
    public static Style createDefaultPointStyle() {

        FeatureTypeStyle featureTypeStyle = sf.createFeatureTypeStyle();
        featureTypeStyle.rules().add(createDefaultPointRule());

        Style style = sf.createStyle();
        style.featureTypeStyles().add(featureTypeStyle);

        return style;
    }

    /**
     * Creates a default {@link Rule} for a point.
     *
     * @return the default rule.
     */
    public static Rule createDefaultPointRule() {
        Graphic graphic = sf.createDefaultGraphic();
        Mark circleMark = sf.getCircleMark();
        circleMark.setFill(sf.createFill(ff.literal("#" + Integer.toHexString(Color.RED.getRGB() & 0xffffff))));
        circleMark.setStroke(sf.createStroke(ff.literal("#" + Integer.toHexString(Color.BLACK.getRGB() & 0xffffff)), ff.literal(DEFAULT_WIDTH)));
        graphic.graphicalSymbols().clear();
        graphic.graphicalSymbols().add(circleMark);
        graphic.setSize(ff.literal(DEFAULT_SIZE));

        PointSymbolizer pointSymbolizer = sf.createPointSymbolizer();
        Rule rule = sf.createRule();
        rule.setName("New rule");
        rule.symbolizers().add(pointSymbolizer);

        pointSymbolizer.setGraphic(graphic);
        return rule;
    }

    /**
     * Creates a default {@link Style} for a polygon.
     *
     * @return the default style.
     */
    public static Style createDefaultPolygonStyle() {
        FeatureTypeStyle featureTypeStyle = sf.createFeatureTypeStyle();
        featureTypeStyle.rules().add(createDefaultPolygonRule());

        Style style = sf.createStyle();
        style.featureTypeStyles().add(featureTypeStyle);

        return style;
    }

    /**
     * Creates a default {@link Rule} for a polygon.
     *
     * @return the default rule.
     */
    public static Rule createDefaultPolygonRule() {
        PolygonSymbolizer polygonSymbolizer = sf.createPolygonSymbolizer();
        polygonSymbolizer.setFill(sf.createFill(ff.literal("#" + Integer.toHexString(Color.RED.getRGB() & 0xffffff))));
        polygonSymbolizer.getFill().setOpacity(ff.literal(0.50));
        polygonSymbolizer.setStroke(sf.createStroke(ff.literal("#" + Integer.toHexString(Color.BLACK.getRGB() & 0xffffff)), ff.literal(DEFAULT_WIDTH)));

        Rule rule = sf.createRule();
        rule.setName("New rule");
        rule.symbolizers().add(polygonSymbolizer);

        return rule;
    }

    /**
     * Creates a default {@link Style} for a line.
     *
     * @return the default style.
     */
    public static Style createDefaultLineStyle() {
        FeatureTypeStyle featureTypeStyle = sf.createFeatureTypeStyle();
        featureTypeStyle.rules().add(createDefaultLineRule());

        Style style = sf.createStyle();
        style.featureTypeStyles().add(featureTypeStyle);

        return style;
    }

    /**
     * Creates a default {@link Rule} for a line.
     *
     * @return the default rule.
     */
    public static Rule createDefaultLineRule() {
        LineSymbolizer lineSymbolizer = sf.createLineSymbolizer();
        lineSymbolizer.setStroke(sf.createStroke(ff.literal("#" + Integer.toHexString(Color.BLACK.getRGB() & 0xffffff)), ff.literal(1)));

        Rule rule = sf.createRule();
        rule.setName("New rule");
        rule.symbolizers().add(lineSymbolizer);

        return rule;
    }

    /**
     * Creates a default {@link TextSymbolizer} for a given type.
     *
     * @return the default symbolizer.
     */
    public static TextSymbolizer createDefaultTextSymbolizer( String type ) {
        LabelPlacement labelPlacement = null;

        switch( type ) {
            case "POINT":
            case "POLYGON":
                labelPlacement = sf.createPointPlacement(sf.createAnchorPoint(ff.literal(0.0), ff.literal(0.0)),
                        sf.createDisplacement(ff.literal(0.0), ff.literal(0.0)), ff.literal(0.0));
                break;
            case "LINE":
                labelPlacement = sf.createLinePlacement(ff.literal(10.0));
                break;

            default:
                throw new IllegalArgumentException();
        }

        Font font = sb.createFont("Arial", false, false, 12); //$NON-NLS-1$
        TextSymbolizer textSymbolizer = sf.createTextSymbolizer(sf.createFill(ff.literal(DEFAULT_COLOR)), new Font[]{font}, null,
                ff.literal("dummy"), labelPlacement, null);

        return textSymbolizer;
    }

    /**
     * Creates a default {@link TextSymbolizer} for a point.
     *
     * @return the default symbolizer.
     */
    public static Symbolizer createDefaultGeometrySymbolizer( String type ) {
        Symbolizer symbolizer = null;
        switch( type ) {
            case "POINT":
                Rule defaultPointRule = createDefaultPointRule();
                symbolizer = defaultPointRule.getSymbolizers()[0];
                break;
            case "POLYGON":
                Rule defaultPolygonRule = createDefaultPolygonRule();
                symbolizer = defaultPolygonRule.getSymbolizers()[0];
                break;
            case "LINE":
                Rule defaultLineRule = createDefaultLineRule();
                symbolizer = defaultLineRule.getSymbolizers()[0];
                break;

            default:
                throw new IllegalArgumentException();
        }

        return symbolizer;
    }

    /**
     * Get the {@link PointSymbolizer} from the given rule.
     *
     * @param rule the rule to check for symbolizers.
     * @return the first symbolizer found.
     */
    public static PointSymbolizer pointSymbolizerFromRule( Rule rule ) {
        List<Symbolizer> symbolizers = rule.symbolizers();
        PointSymbolizer pointSymbolizer = null;
        for( Symbolizer symbolizer : symbolizers ) {
            if (symbolizer instanceof PointSymbolizer) {
                pointSymbolizer = (PointSymbolizer) symbolizer;
                break;
            }
        }
        if (pointSymbolizer == null) {
            throw new IllegalArgumentException();
        }
        return pointSymbolizer;
    }

    /**
     * Get the {@link PolygonSymbolizer} from the given rule.
     *
     * @param rule the rule to check for symbolizers.
     * @return the first symbolizer found.
     */
    public static PolygonSymbolizer polygonSymbolizerFromRule( Rule rule ) {
        List<Symbolizer> symbolizers = rule.symbolizers();
        PolygonSymbolizer polygonSymbolizer = null;
        for( Symbolizer symbolizer : symbolizers ) {
            if (symbolizer instanceof PolygonSymbolizer) {
                polygonSymbolizer = (PolygonSymbolizer) symbolizer;
                break;
            }
        }
        if (polygonSymbolizer == null) {
            throw new IllegalArgumentException();
        }
        return polygonSymbolizer;
    }

    /**
     * Get the {@link LineSymbolizer} from the given rule.
     *
     * @param rule the rule to check for symbolizers.
     * @return the first symbolizer found.
     */
    public static LineSymbolizer lineSymbolizerFromRule( Rule rule ) {
        List<Symbolizer> symbolizers = rule.symbolizers();
        LineSymbolizer lineSymbolizer = null;
        for( Symbolizer symbolizer : symbolizers ) {
            if (symbolizer instanceof LineSymbolizer) {
                lineSymbolizer = (LineSymbolizer) symbolizer;
                break;
            }
        }
        if (lineSymbolizer == null) {
            throw new IllegalArgumentException();
        }
        return lineSymbolizer;
    }

    /**
     * Change the mark shape in a rule.
     *
     * @param rule the rule of which the mark has to be changed.
     * @param wellKnownMarkName the name of the new mark.
     */
    public static void substituteMark( Rule rule, String wellKnownMarkName ) {
        PointSymbolizer pointSymbolizer = pointSymbolizerFromRule(rule);
        Mark oldMark = SLD.mark(pointSymbolizer);

        Graphic graphic = SLD.graphic(pointSymbolizer);
        graphic.graphicalSymbols().clear();

        Mark mark = sf.createMark();
        mark.setWellKnownName(ff.literal(wellKnownMarkName));
        if (oldMark != null) {
            mark.setFill(oldMark.getFill());
            mark.setStroke(oldMark.getStroke());
        }
        graphic.graphicalSymbols().add(mark);
    }

    /**
     * Change the external graphic in a rule.
     *
     * @param rule the rule of which the external graphic has to be changed.
     * @param externalGraphicsUrl the path of the image.
     */
    public static void substituteExternalGraphics( Rule rule, URL externalGraphicsUrl ) {
        String urlString = externalGraphicsUrl.toString();
        String format = "";
        if (urlString.toLowerCase().endsWith(".png")) {
            format = "image/png";
        } else if (urlString.toLowerCase().endsWith(".jpg")) {
            format = "image/jpg";
        } else if (urlString.toLowerCase().endsWith(".svg")) {
            format = "image/svg+xml";
        } else {
            urlString = "";
            try {
                externalGraphicsUrl = new URL("file:");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        PointSymbolizer pointSymbolizer = pointSymbolizerFromRule(rule);
        Graphic graphic = SLD.graphic(pointSymbolizer);
        graphic.graphicalSymbols().clear();
        ExternalGraphic exGraphic = sf.createExternalGraphic(externalGraphicsUrl, format);

        graphic.graphicalSymbols().add(exGraphic);
    }

    public static String getFormat( String path ) {
        String format = "";
        if (path.toLowerCase().endsWith(".png")) {
            format = "image/png";
        } else if (path.toLowerCase().endsWith(".jpg")) {
            format = "image/jpg";
        } else if (path.toLowerCase().endsWith(".gif")) {
            format = "image/gif";
        } else if (path.toLowerCase().endsWith(".svg")) {
            format = "image/svg+xml";
        }
        return format;
    }

    /**
     * Get the format of an {@link ExternalGraphic} from its path or name.
     *
     * @param name the path or file name to test against.
     * @return teh format definition.
     */
    public static String getExternalGraphicFormat( String name ) {
        String format = "";
        if (name.toLowerCase().endsWith(".png")) {
            format = "image/png";
        } else if (name.toLowerCase().endsWith(".jpg")) {
            format = "image/jpg";
        } else if (name.toLowerCase().endsWith(".gif")) {
            format = "image/gif";
        } else if (name.toLowerCase().endsWith(".svg")) {
            format = "image/svg+xml";
        } else {
            return null;
        }

        return format;
    }

    /**
     * Changes the size of a mark inside a rule.
     *
     * @param rule the {@link Rule}.
     * @param newSize the new size.
     */
    public static void changeMarkSize( Rule rule, int newSize ) {
        PointSymbolizer pointSymbolizer = pointSymbolizerFromRule(rule);
        Graphic graphic = SLD.graphic(pointSymbolizer);
        graphic.setSize(ff.literal(newSize));
        // Mark oldMark = SLDs.mark(pointSymbolizer);
        // oldMark.setSize(ff.literal(newSize));
        // Graphic graphic = SLDs.graphic(pointSymbolizer);
    }

    /**
     * Changes the rotation value inside a rule.
     *
     * @param rule the {@link Rule}.
     * @param newRotation the new rotation value in degrees.
     */
    public static void changeRotation( Rule rule, int newRotation ) {
        PointSymbolizer pointSymbolizer = pointSymbolizerFromRule(rule);
        Graphic graphic = SLD.graphic(pointSymbolizer);
        graphic.setRotation(ff.literal(newRotation));
        // Mark oldMark = SLDs.mark(pointSymbolizer);
        // oldMark.setSize(ff.literal(newRotation));
    }

    /**
     * Get the offset from a {@link Symbolizer}.
     *
     * @param symbolizer the symbolizer.
     * @return the offset.
     */
    @SuppressWarnings("rawtypes")
    public static Point2D getOffset( Symbolizer symbolizer ) {
        Expression geometry = symbolizer.getGeometry();
        if (geometry != null) {
            if (geometry instanceof FilterFunction_offset) {
                FilterFunction_offset offsetFunction = (FilterFunction_offset) geometry;
                List parameters = offsetFunction.getParameters();
                Expression xOffsetExpr = (Expression) parameters.get(1);
                Expression yOffsetExpr = (Expression) parameters.get(2);
                Double xOffsetDouble = xOffsetExpr.evaluate(null, Double.class);
                Double yOffsetDouble = yOffsetExpr.evaluate(null, Double.class);
                if (xOffsetDouble != null && yOffsetDouble != null) {
                    Point2D.Double point = new Point2D.Double(xOffsetDouble, yOffsetDouble);
                    return point;
                }
            }
        }
        return null;
    }

    /**
     * Sets the offset in a symbolizer.
     *
     * @param symbolizer the symbolizer.
     * @param text the text representing the offsets in the CSV form.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setOffset( Symbolizer symbolizer, String text ) {
        if (text.indexOf(',') == -1) {
            return;
        }
        String[] split = text.split(",");
        if (split.length != 2) {
            return;
        }
        double xOffset = Double.parseDouble(split[0]);
        double yOffset = Double.parseDouble(split[1]);

        Expression geometry = symbolizer.getGeometry();
        if (geometry != null) {
            if (geometry instanceof FilterFunction_offset) {
                FilterFunction_offset offsetFunction = (FilterFunction_offset) geometry;
                List parameters = offsetFunction.getParameters();
                parameters.set(1, ff.literal(xOffset));
                parameters.set(2, ff.literal(yOffset));
            }
        } else {
            Function function = ff.function("offset", ff.property("the_geom"), ff.literal(xOffset), ff.literal(yOffset));
            symbolizer.setGeometry(function);
        }
    }

    /**
     * Converts a list of {@link Rule}s to a {@link Style} with the given name.
     *
     * @param rules the list of rules.
     * @param name the name of the new style.
     * @param oneFeaturetypestylePerRule switch to create a {@link FeatureTypeStyle} per {@link Rule}.
     * @return the new style created.
     */
    public static Style rulesToStyle( List<Rule> rules, String name, boolean oneFeaturetypestylePerRule ) {
        Style namedStyle = sf.createStyle();
        if (!oneFeaturetypestylePerRule) {
            FeatureTypeStyle featureTypeStyle = sf.createFeatureTypeStyle();
            List<Rule> currentRules = featureTypeStyle.rules();
            for( int i = 0; i < rules.size(); i++ ) {
                Rule rule = rules.get(i);
                currentRules.add(rule);
            }
            namedStyle.featureTypeStyles().add(featureTypeStyle);
        } else {
            for( int i = 0; i < rules.size(); i++ ) {
                FeatureTypeStyle featureTypeStyle = sf.createFeatureTypeStyle();
                Rule rule = rules.get(i);
                featureTypeStyle.rules().add(rule);
                namedStyle.featureTypeStyles().add(featureTypeStyle);
            }
        }
        namedStyle.setName(name);
        return namedStyle;
    }

    /**
     * Converts a style to its string representation to be written to file.
     *
     * @param style the style to convert.
     * @return the style string.
     * @throws Exception
     */
    public static String styleToString( Style style ) throws Exception {
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        UserLayer layer = sf.createUserLayer();
        layer.setLayerFeatureConstraints(new FeatureTypeConstraint[]{null});
        sld.addStyledLayer(layer);
        layer.addUserStyle(style);

        SLDTransformer aTransformer = new SLDTransformer();
        aTransformer.setIndentation(4);
        String xml = aTransformer.transform(sld);
        return xml;
    }

    public static void main(String[] args) throws Exception {
        Style style=SLDUtil.createDefaultPointStyle();
        String xml=SLDUtil.styleToString(style);
        System.err.print(xml);
    }

}