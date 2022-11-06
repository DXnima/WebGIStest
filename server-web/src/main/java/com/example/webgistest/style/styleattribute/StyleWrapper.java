package com.example.webgistest.style.styleattribute;

import com.example.webgistest.style.utils.StyleUtil;
import org.geotools.styling.*;
import org.geotools.xml.styling.SLDTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A wrapper for the {@link Style} object to ease gui use.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class StyleWrapper {
    private final Style style;
    private String name;
    private final List<FeatureTypeStyleWrapper> featureTypeStylesWrapperList = new ArrayList<FeatureTypeStyleWrapper>();

    public StyleWrapper(Style style) {
        this.style = style;
        name = style.getName();

        List<FeatureTypeStyle> featureTypeStyles = style.featureTypeStyles();
        for (FeatureTypeStyle featureTypeStyle : featureTypeStyles) {
            FeatureTypeStyleWrapper fstW = new FeatureTypeStyleWrapper(featureTypeStyle, this);
            featureTypeStylesWrapperList.add(fstW);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        style.setName(name);
    }

    /**
     * Getter for the list of {@link FeatureTypeStyleWrapper}s.
     *
     * @return an unmodifiable list of {@link FeatureTypeStyleWrapper}.
     * To add or remove items use {@link #addFeatureTypeStyle(FeatureTypeStyle)}
     * and {@link #removeFeatureTypeStyle(FeatureTypeStyleWrapper)}.
     */
    public List<FeatureTypeStyleWrapper> getFeatureTypeStylesWrapperList() {
        return Collections.unmodifiableList(featureTypeStylesWrapperList);
    }

    public void addRule(Rule rule) {
        if (featureTypeStylesWrapperList.size() > 0) {
            FeatureTypeStyleWrapper featureTypeStyleWrapper = featureTypeStylesWrapperList.get(0);
            featureTypeStyleWrapper.addRule(rule, null);
        }
    }

    /**
     * Facility to get the first rule, if available.
     *
     * @return the first rule or <code>null</code>.
     */
    public RuleWrapper getFirstRule() {
        List<RuleWrapper> list = getAllRule();
        if (list != null && getAllRule().size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Facility to get the all rule, if available.
     *
     * @return the first rule or <code>null</code>.
     */
    public List<RuleWrapper> getAllRule() {
        if (featureTypeStylesWrapperList.size() > 0) {
            FeatureTypeStyleWrapper featureTypeStyleWrapper = featureTypeStylesWrapperList.get(0);
            List<RuleWrapper> rulesWrapperList = featureTypeStyleWrapper.getRulesWrapperList();
            if (rulesWrapperList.size() > 0) {
                return rulesWrapperList;
            }
        }
        return null;
    }

    /**
     * Add a supplied or new {@link FeatureTypeStyle} to the {@link Style}.
     *
     * @param tmpFts the new {@link FeatureTypeStyle} or null to create a new one.
     * @return the {@link FeatureTypeStyleWrapper} for the new {@link FeatureTypeStyle}.
     */
    public FeatureTypeStyleWrapper addFeatureTypeStyle(FeatureTypeStyle tmpFts) {
        if (tmpFts == null)
            tmpFts = StyleUtil.sf.createFeatureTypeStyle();

        style.featureTypeStyles().add(0, tmpFts);

        FeatureTypeStyleWrapper wrapper = new FeatureTypeStyleWrapper(tmpFts, this);
        featureTypeStylesWrapperList.add(0, wrapper);
        return wrapper;
    }

    /**
     * Remove a {@link FeatureTypeStyleWrapper} from the list.
     *
     * @param ftsW the {@link FeatureTypeStyle} to remove.
     */
    public void removeFeatureTypeStyle(FeatureTypeStyleWrapper ftsW) {
        FeatureTypeStyle fts = ftsW.getFeatureTypeStyle();
        style.featureTypeStyles().remove(fts);
        featureTypeStylesWrapperList.remove(ftsW);
    }

    /**
     * Clear all the {@link FeatureTypeStyle}s and {@link FeatureTypeStyleWrapper}s.
     */
    public void clear() {
        style.featureTypeStyles().clear();
        featureTypeStylesWrapperList.clear();
    }

    /**
     * Converts a style to its string representation to be written to file.
     * <p>
     * style the style to convert.
     *
     * @return the style string.
     * @throws Exception
     */
    public String toXml() throws Exception {
        StyledLayerDescriptor sld = StyleUtil.sf.createStyledLayerDescriptor();
        UserLayer layer = StyleUtil.sf.createUserLayer();
        layer.setLayerFeatureConstraints(new FeatureTypeConstraint[]{null});
        sld.addStyledLayer(layer);
        layer.addUserStyle(style);

        SLDTransformer aTransformer = new SLDTransformer();
        aTransformer.setIndentation(4);
        String xml = aTransformer.transform(sld);
        return xml;
    }

    public Style getStyle() {
        return style;
    }

    /**
     * Swap two elements of the list.
     *
     * @param src  the position first element.
     * @param dest the position second element.
     */
    public void swap(int src, int dest) {
        List<FeatureTypeStyle> ftss = style.featureTypeStyles();
        if (src >= 0 && src < ftss.size() && dest >= 0 && dest < ftss.size()) {
            Collections.swap(ftss, src, dest);
            Collections.swap(featureTypeStylesWrapperList, src, dest);
        }
    }

}
