package com.example.webgistest.style.styleattribute;

import com.example.webgistest.style.utils.StyleUtil;
import org.geotools.styling.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper for the {@link Rule} object to ease gui use.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class RuleWrapper {
    private final Rule rule;
    private String name;
    private String maxScale;
    private String minScale;
    private final List<SymbolizerWrapper> symbolizersWrapperList = new ArrayList<SymbolizerWrapper>();
    private final FeatureTypeStyleWrapper parent;

    public RuleWrapper(Rule rule, FeatureTypeStyleWrapper parent) {
        this.rule = rule;
        this.parent = parent;

        name = rule.getName();
        try {
            maxScale = String.valueOf(rule.getMaxScaleDenominator());
            minScale = String.valueOf(rule.getMinScaleDenominator());
        } catch (Exception e) {
            maxScale = StyleUtil.DEFAULT_MAXSCALE;
            minScale = StyleUtil.DEFAULT_MINSCALE;
        }

        List<Symbolizer> symbolizers = rule.symbolizers();
        for (Symbolizer symbolizer : symbolizers) {
            SymbolizerWrapper wrapper = getWrapper(symbolizer);
            symbolizersWrapperList.add(wrapper);
        }
    }

    public FeatureTypeStyleWrapper getParent() {
        return parent;
    }

    /**
     * getter for the {@link Rule} that the {@link RuleWrapper} wraps.
     *
     * @return the backed rule.
     */
    public Rule getRule() {
        return rule;
    }

    private SymbolizerWrapper getWrapper(Symbolizer symbolizer) {
        SymbolizerWrapper symbolizerWrapper = null;
        if (symbolizer instanceof PointSymbolizer) {
            symbolizerWrapper = new PointSymbolizerWrapper(symbolizer, this);
        } else if (symbolizer instanceof LineSymbolizer) {
            symbolizerWrapper = new LineSymbolizerWrapper(symbolizer, this);
        } else if (symbolizer instanceof PolygonSymbolizer) {
            symbolizerWrapper = new PolygonSymbolizerWrapper(symbolizer, this);
        } else if (symbolizer instanceof TextSymbolizer) {
            symbolizerWrapper = new TextSymbolizerWrapper(symbolizer, this, getType());
        } else if (symbolizer instanceof RasterSymbolizer) {
            return null;
        }

        return symbolizerWrapper;
    }

    /**
     * Returns the type of geometry/raster that the {@link SymbolizerWrapper} represents.
     *
     * @return the symbolizer type.
     */
    public String getType() {
        SymbolizerWrapper geometrySymbolizersWrapper = getGeometrySymbolizersWrapper();
        if (geometrySymbolizersWrapper == null) {
            return null;
        }
        Symbolizer symbolizer = geometrySymbolizersWrapper.getSymbolizer();
        if (symbolizer instanceof PointSymbolizer) {
            return "POINT";
        } else if (symbolizer instanceof LineSymbolizer) {
            return "LINE";
        } else if (symbolizer instanceof PolygonSymbolizer) {
            return "POLYGON";
        } else if (symbolizer instanceof RasterSymbolizer) {
            return "RASTER";
        }
        return null;
    }

    /**
     * Getter for the used {@link SymbolizerWrapper}, for point, line or polygon.
     *
     * <p>Currently only one {@link Symbolizer} is supported in editing, so just the first is used.</p>
     *
     * @return the used {@link Symbolizer}.
     */
    public SymbolizerWrapper getGeometrySymbolizersWrapper() {
        for (SymbolizerWrapper symbolizerWrapper : symbolizersWrapperList) {
            if (!symbolizerWrapper.isTextSymbolizer()) {
                return symbolizerWrapper;
            }
        }

        DummySymbolizerWrapper geometrySymbolizersWrapper = new DummySymbolizerWrapper(
                StyleUtil.createDefaultGeometrySymbolizer("POINT"), null);
        return geometrySymbolizersWrapper;
    }

    /**
     * Getter for the used {@link TextSymbolizerWrapper}.
     *
     * <p>Currently only one {@link TextSymbolizer} is supported in editing, so just the first is used.</p>
     *
     * @return the used {@link TextSymbolizer}.
     */
    public TextSymbolizerWrapper getTextSymbolizersWrapper() {
        for (SymbolizerWrapper symbolizerWrapper : symbolizersWrapperList) {
            if (symbolizerWrapper.isTextSymbolizer()) {
                return (TextSymbolizerWrapper) symbolizerWrapper;
            }
        }
        return null;
    }

    /**
     * Remove the {@link TextSymbolizerWrapper} from the ruleWrapper.
     */
    public void removeTextSymbolizersWrapper() {
        List<SymbolizerWrapper> removeSW = new ArrayList<SymbolizerWrapper>();
        List<Symbolizer> removeS = new ArrayList<Symbolizer>();

        List<Symbolizer> symbolizers = rule.symbolizers();
        for (SymbolizerWrapper symbolizerWrapper : symbolizersWrapperList) {
            if (symbolizerWrapper.isTextSymbolizer()) {
                Symbolizer symbolizer = symbolizerWrapper.getSymbolizer();
                removeSW.add(symbolizerWrapper);
                removeS.add(symbolizer);
            }
        }

        symbolizersWrapperList.removeAll(removeSW);
        symbolizers.removeAll(removeS);
    }

    /**
     * Add a supplied or new {@link Symbolizer} to the {@link Rule}.
     *
     * @param newSymbolizer   the new {@link Symbolizer} or null to create a new one.
     * @param symbolizerClass the class in the case the symbolizer has to be created.
     * @return the {@link SymbolizerWrapper} for the new {@link Symbolizer}.
     */
    public <T> T addSymbolizer(Symbolizer newSymbolizer, Class<T> symbolizerClass) {
        SymbolizerWrapper wrapper = null;
        if (newSymbolizer != null) {
            if (newSymbolizer instanceof PointSymbolizer) {
                wrapper = new PointSymbolizerWrapper(newSymbolizer, this);
            } else if (newSymbolizer instanceof LineSymbolizer) {
                wrapper = new LineSymbolizerWrapper(newSymbolizer, this);
            } else if (newSymbolizer instanceof PolygonSymbolizer) {
                wrapper = new PolygonSymbolizerWrapper(newSymbolizer, this);
            } else if (newSymbolizer instanceof TextSymbolizer) {
                wrapper = new TextSymbolizerWrapper(newSymbolizer, this, getType());
            } else if (newSymbolizer instanceof RasterSymbolizer) {
                // FIXME
                return null;
            }
        } else {
            if (symbolizerClass.isAssignableFrom(PointSymbolizerWrapper.class)) {
                newSymbolizer = StyleUtil.sf.createPointSymbolizer();
                wrapper = new PointSymbolizerWrapper(newSymbolizer, this);
            } else if (symbolizerClass.isAssignableFrom(LineSymbolizerWrapper.class)) {
                newSymbolizer = StyleUtil.sf.createLineSymbolizer();
                wrapper = new LineSymbolizerWrapper(newSymbolizer, this);
            } else if (symbolizerClass.isAssignableFrom(PolygonSymbolizerWrapper.class)) {
                newSymbolizer = StyleUtil.sf.createPolygonSymbolizer();
                wrapper = new PolygonSymbolizerWrapper(newSymbolizer, this);
            } else if (symbolizerClass.isAssignableFrom(TextSymbolizerWrapper.class)) {
                newSymbolizer = StyleUtil.sf.createTextSymbolizer();
                wrapper = new TextSymbolizerWrapper(newSymbolizer, this, getType());
            } else if (symbolizerClass.isAssignableFrom(RasterSymbolizer.class)) {
                // FIXME
                return null;
            }
        }

        rule.symbolizers().add(newSymbolizer);

        symbolizersWrapperList.add(wrapper);

        return symbolizerClass.cast(wrapper);
    }

    /**
     * Clear all the {@link Symbolizer}s and {@link SymbolizerWrapper}s.
     */
    public void clear() {
        rule.symbolizers().clear();
        symbolizersWrapperList.clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        rule.setName(name);
    }

    public String getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(String maxScale) {
        this.maxScale = maxScale;
        try {
            rule.setMaxScaleDenominator(Double.parseDouble(maxScale));
        } catch (Exception e) {
            rule.setMaxScaleDenominator(Double.POSITIVE_INFINITY);
        }
    }

    public String getMinScale() {
        return minScale;
    }

    public void setMinScale(String minScale) {
        this.minScale = minScale;
        try {
            rule.setMinScaleDenominator(Double.parseDouble(minScale));
        } catch (Exception e) {
            rule.setMinScaleDenominator(Double.parseDouble(StyleUtil.DEFAULT_MINSCALE));
        }
    }

}
