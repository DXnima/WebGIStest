package com.example.webgistest.style.styleattribute;

import com.example.webgistest.style.stylevariable.VendorOptions;
import com.example.webgistest.style.utils.StyleUtil;
import org.geotools.styling.*;
import org.opengis.filter.expression.Expression;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A wrapper for a {@link TextSymbolizer} to ease interaction with gui.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class TextSymbolizerWrapper extends SymbolizerWrapper {

    private String fontFamily;
    private String fontStyle;
    private String fontWeight;
    private String fontSize;
    private String color;
    private String opacity;
    private String haloColor;
    private String anchorX;
    private String anchorY;
    private String displacementX;
    private String displacementY;
    private String rotation;
    private String maxDisplacementVO;
    private String repeatVO;
    private String autoWrapVO;
    private String spaceAroundVO;
    private Font font;

    private final TextSymbolizer textSymbolizer;
    private Fill fill;
    private Halo halo;
    private Fill haloFill;
    private PointPlacement pointPlacement;
    private LinePlacement linePlacement;
    private AnchorPoint anchorPoint;
    private Displacement displacement;
    private String haloRadius;
    private String initialGap;
    private String perpendicularOffset;
    private String followLineVO;
    private String maxAngleDeltaVO;
    private final String geomType;
    private String labelName;

    public TextSymbolizerWrapper(Symbolizer symbolizer, RuleWrapper parent, String geomType) {
        super(symbolizer, parent);
        this.geomType = geomType;

        textSymbolizer = (TextSymbolizer) symbolizer;

        Expression labelExpression = textSymbolizer.getLabel();
        if (labelExpression != null) {
            labelName = expressionToString(labelExpression);
        }

        font = textSymbolizer.getFont();
        if (font != null) {
            List<Expression> family = font.getFamily();
            fontFamily = family.get(0).evaluate(null, String.class);

            Expression styleExpression = font.getStyle();
            fontStyle = styleExpression.evaluate(null, String.class);

            Expression styleWeight = font.getWeight();
            fontWeight = styleWeight.evaluate(null, String.class);

            Expression styleSize = font.getSize();
            fontSize = styleSize.evaluate(null, String.class);
        }

        fill = textSymbolizer.getFill();
        if (fill != null) {
            Expression colorExpression = fill.getColor();
            color = colorExpression.evaluate(null, String.class);
            Expression opacityExpression = fill.getOpacity();
            if (opacityExpression != null) {
                opacity = expressionToString(opacityExpression);
            }
        }

        halo = textSymbolizer.getHalo();
        if (halo != null) {
            haloFill = halo.getFill();
            Expression haloColorExpression = haloFill.getColor();
            haloColor = haloColorExpression.evaluate(null, String.class);

            Expression haloRadiusExpression = halo.getRadius();
            haloRadius = haloRadiusExpression.evaluate(null, String.class);
        }

        LabelPlacement labelPlacement = textSymbolizer.getLabelPlacement();
        if (geomType != null) {
            switch (geomType) {
                case "POINT":
                case "POLYGON":
                    if (labelPlacement instanceof PointPlacement) {
                        pointPlacement = (PointPlacement) labelPlacement;
                        if (pointPlacement != null) {
                            anchorPoint = pointPlacement.getAnchorPoint();
                            if (anchorPoint != null) {
                                Expression anchorPointXExpression = anchorPoint.getAnchorPointX();
                                anchorX = anchorPointXExpression.evaluate(null, String.class);
                                Expression anchorPointYExpression = anchorPoint.getAnchorPointY();
                                anchorY = anchorPointYExpression.evaluate(null, String.class);
                            }

                            displacement = pointPlacement.getDisplacement();
                            if (displacement != null) {
                                Expression displacementXExpression = displacement.getDisplacementX();
                                displacementX = displacementXExpression.evaluate(null, String.class);
                                Expression displacementYExpression = displacement.getDisplacementY();
                                displacementY = displacementYExpression.evaluate(null, String.class);
                            } else {
                                displacementX = "0.0"; //$NON-NLS-1$
                                displacementY = "0.0"; //$NON-NLS-1$
                            }

                            Expression rotationExpression = pointPlacement.getRotation();
                            rotation = expressionToString(rotationExpression);
                        }
                    }
                    break;
                case "LINE":
                    if (labelPlacement instanceof LinePlacement) {
                        linePlacement = (LinePlacement) labelPlacement;
                        if (linePlacement != null) {
                            Expression initialGapExpression = linePlacement.getInitialGap();
                            if (initialGapExpression != null)
                                initialGap = initialGapExpression.evaluate(null, String.class);

                            Expression perpendicularOffsetExpression = linePlacement.getPerpendicularOffset();
                            if (perpendicularOffset != null)
                                perpendicularOffset = perpendicularOffsetExpression.evaluate(null, String.class);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        /*
         * vendoroptions
         */
        Map<String, String> vendorOptions = textSymbolizer.getOptions();
        Set<Entry<String, String>> entrySet = vendorOptions.entrySet();
        for (Entry<String, String> entry : entrySet) {
            String key = entry.getKey();
            String value = entry.getValue();

            switch (VendorOptions.toVendorOption(key)) {
                case VENDOROPTION_MAXDISPLACEMENT:
                    maxDisplacementVO = value;
                    break;
                case VENDOROPTION_REPEAT:
                    repeatVO = value;
                    break;
                case VENDOROPTION_AUTOWRAP:
                    autoWrapVO = value;
                    break;
                case VENDOROPTION_SPACEAROUND:
                    spaceAroundVO = value;
                    break;
                case VENDOROPTION_FOLLOWLINE:
                    followLineVO = value;
                    break;
                case VENDOROPTION_MAXANGLEDELTA:
                    maxAngleDeltaVO = value;
                    break;
                default:
                    break;
            }
        }
    }

    private void checkFontExists() {
        if (font == null) {
            font = StyleUtil.sb.createFont("Arial", false, false, 12); //$NON-NLS-1$
            textSymbolizer.setFont(font);
        }
    }

    private void checkFillExists() {
        if (fill == null) {
            fill = StyleUtil.sb.createFill(StyleUtil.ff.literal(StyleUtil.DEFAULT_COLOR));
            textSymbolizer.setFill(fill);
        }
    }

    private void checkHaloFillExists() {
        if (haloFill == null) {
            haloFill = StyleUtil.sb.createFill(StyleUtil.ff.literal(StyleUtil.DEFAULT_COLOR));
            checkHaloExists();
            halo.setFill(haloFill);
        }
    }

    private void checkHaloExists() {
        if (halo == null) {
            halo = StyleUtil.sb.createHalo();
            textSymbolizer.setHalo(halo);
        }
    }

    private void checkPlacementExists() {
        switch (geomType) {
            case "POINT":
            case "POLYGON":
                if (pointPlacement == null) {
                    pointPlacement = StyleUtil.sb.createPointPlacement();
                    textSymbolizer.setLabelPlacement(pointPlacement);
                }
                break;
            case "LINE":
                if (linePlacement == null) {
                    linePlacement = StyleUtil.sb.createLinePlacement(0.0);
                    textSymbolizer.setLabelPlacement(linePlacement);
                }
                break;

            default:
                break;
        }
    }

    private void checkAnchorPointExists() {
        if (anchorPoint == null) {
            anchorPoint = StyleUtil.sb.createAnchorPoint(0.5, 0.5);
            checkPlacementExists();
            pointPlacement.setAnchorPoint(anchorPoint);
        }
    }

    private void checkDisplacementExists() {
        if (displacement == null) {
            displacement = StyleUtil.sb.createDisplacement(0.0, 0.0);
            checkPlacementExists();
            pointPlacement.setDisplacement(displacement);
        }
    }

    public void setLabelName(String labelName, boolean fromField) {
        this.labelName = labelName;
        if (fromField) {
            textSymbolizer.setLabel(StyleUtil.ff.property(labelName));
        } else {
            textSymbolizer.setLabel(StyleUtil.ff.literal(labelName));
        }
    }

    public void setFont(Font font) {
        this.font = font;
        textSymbolizer.setFont(font);
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
        checkFontExists();
        font.getFamily().set(0, StyleUtil.ff.literal(fontFamily));
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
        checkFontExists();
        font.setStyle(StyleUtil.ff.literal(fontStyle));
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
        checkFontExists();
        font.setWeight(StyleUtil.ff.literal(fontWeight));
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
        checkFontExists();
        font.setSize(StyleUtil.ff.literal(fontSize));
    }

    public void setColor(String color) {
        this.color = color;
        checkFillExists();
        fill.setColor(StyleUtil.ff.literal(color));
    }

    public void setOpacity(String opacity, boolean fromField) {
        this.opacity = opacity;
        checkFillExists();
        if (fromField) {
            fill.setOpacity(StyleUtil.ff.property(opacity));
        } else {
            fill.setOpacity(StyleUtil.ff.literal(opacity));
        }
    }

    public void setHaloColor(String haloColor) {
        this.haloColor = haloColor;
        checkHaloFillExists();
        haloFill.setColor(StyleUtil.ff.literal(haloColor));
    }

    public void setHaloRadius(String haloRadius) {
        this.haloRadius = haloRadius;
        checkHaloExists();
        halo.setRadius(StyleUtil.ff.literal(haloRadius));
    }

    public void setAnchorX(String anchorX) {
        this.anchorX = anchorX;
        checkAnchorPointExists();
        anchorPoint.setAnchorPointX(StyleUtil.ff.literal(anchorX));
    }

    public void setAnchorY(String anchorY) {
        this.anchorY = anchorY;
        checkAnchorPointExists();
        anchorPoint.setAnchorPointY(StyleUtil.ff.literal(anchorY));
    }

    public void setDisplacement(String displacement) {
        if (displacement == null || displacement.indexOf(',') == -1) {
            return;
        }
        String[] split = displacement.split(","); //$NON-NLS-1$
        try {
            Double.parseDouble(split[0]);
            Double.parseDouble(split[1]);
            setDisplacementX(split[0]);
            setDisplacementY(split[1]);
        } catch (Exception e) {
            // ignore wrong stuff
        }
    }

    public void setDisplacementX(String displacementX) {
        this.displacementX = displacementX;
        checkDisplacementExists();
        displacement.setDisplacementX(StyleUtil.ff.literal(displacementX));
    }

    public void setDisplacementY(String displacementY) {
        this.displacementY = displacementY;
        checkDisplacementExists();
        displacement.setDisplacementY(StyleUtil.ff.literal(displacementY));
    }

    public void setRotation(String rotation, boolean fromField) {
        this.rotation = rotation;
        checkPlacementExists();
        if (fromField) {
            pointPlacement.setRotation(StyleUtil.ff.property(rotation));
        } else {
            pointPlacement.setRotation(StyleUtil.ff.literal(rotation));
        }
    }

    public void setInitialGap(String initialGap) {
        this.initialGap = initialGap;
        checkPlacementExists();
        linePlacement.setInitialGap(StyleUtil.ff.literal(initialGap));
    }

    public void setPerpendicularOffset(String perpendicularOffset) {
        this.perpendicularOffset = perpendicularOffset;
        checkPlacementExists();
        linePlacement.setPerpendicularOffset(StyleUtil.ff.literal(perpendicularOffset));
    }

    public void setMaxDisplacementVO(String maxDisplacementVO) {
        this.maxDisplacementVO = maxDisplacementVO;
        if (maxDisplacementVO == null || maxDisplacementVO.equals("")) { //$NON-NLS-1$
            textSymbolizer.getOptions().remove(VendorOptions.VENDOROPTION_MAXDISPLACEMENT.toString());
        } else {
            textSymbolizer.getOptions().put(VendorOptions.VENDOROPTION_MAXDISPLACEMENT.toString(), maxDisplacementVO);
        }
    }

    public void setRepeatVO(String repeatVO) {
        this.repeatVO = repeatVO;
        if (repeatVO == null || repeatVO.equals("") || !geomType.equals("LINE")) { //$NON-NLS-1$
            textSymbolizer.getOptions().remove(VendorOptions.VENDOROPTION_REPEAT.toString());
        } else {
            textSymbolizer.getOptions().put(VendorOptions.VENDOROPTION_REPEAT.toString(), repeatVO);
        }
    }

    public void setAutoWrapVO(String autoWrapVO) {
        this.autoWrapVO = autoWrapVO;
        if (autoWrapVO == null || autoWrapVO.equals("")) { //$NON-NLS-1$
            textSymbolizer.getOptions().remove(VendorOptions.VENDOROPTION_AUTOWRAP.toString());
        } else {
            textSymbolizer.getOptions().put(VendorOptions.VENDOROPTION_AUTOWRAP.toString(), autoWrapVO);
        }
    }

    public void setSpaceAroundVO(String spaceAroundVO) {
        this.spaceAroundVO = spaceAroundVO;
        if (spaceAroundVO == null || spaceAroundVO.equals("")) { //$NON-NLS-1$
            textSymbolizer.getOptions().remove(VendorOptions.VENDOROPTION_SPACEAROUND.toString());
        } else {
            textSymbolizer.getOptions().put(VendorOptions.VENDOROPTION_SPACEAROUND.toString(), spaceAroundVO);
        }
    }

    public void setFollowLineVO(String followLineVO) {
        this.followLineVO = followLineVO;
        if (followLineVO == null || followLineVO.equals("") || !geomType.equals("LINE")) { //$NON-NLS-1$
            textSymbolizer.getOptions().remove(VendorOptions.VENDOROPTION_FOLLOWLINE.toString());
        } else {
            textSymbolizer.getOptions().put(VendorOptions.VENDOROPTION_FOLLOWLINE.toString(), followLineVO);
        }
    }

    public void setMaxAngleDeltaVO(String maxAngleDeltaVO) {
        this.maxAngleDeltaVO = maxAngleDeltaVO;
        if (maxAngleDeltaVO == null || maxAngleDeltaVO.equals("") || !geomType.equals("LINE")) { //$NON-NLS-1$
            textSymbolizer.getOptions().remove(VendorOptions.VENDOROPTION_MAXANGLEDELTA.toString());
        } else {
            textSymbolizer.getOptions().put(VendorOptions.VENDOROPTION_MAXANGLEDELTA.toString(), maxAngleDeltaVO);
        }
    }

    // getters
    public String getLabelName() {
        return labelName;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public String getFontSize() {
        return fontSize;
    }

    public String getColor() {
        return color;
    }

    public String getOpacity() {
        return opacity;
    }

    public String getHaloColor() {
        return haloColor;
    }

    public String getHaloRadius() {
        return haloRadius;
    }

    public String getAnchorX() {
        return anchorX;
    }

    public String getAnchorY() {
        return anchorY;
    }

    public String getDisplacementX() {
        return displacementX;
    }

    public String getDisplacementY() {
        return displacementY;
    }

    public String getRotation() {
        return rotation;
    }

    public String getInitialGap() {
        return initialGap;
    }

    public String getPerpendicularOffset() {
        return perpendicularOffset;
    }

    public String getMaxDisplacementVO() {
        return maxDisplacementVO;
    }

    public String getRepeatVO() {
        return repeatVO;
    }

    public String getAutoWrapVO() {
        return autoWrapVO;
    }

    public String getSpaceAroundVO() {
        return spaceAroundVO;
    }

    public String getFollowLineVO() {
        return followLineVO;
    }

    public String getMaxAngleDeltaVO() {
        return maxAngleDeltaVO;
    }
}
