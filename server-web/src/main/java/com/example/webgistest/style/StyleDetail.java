package com.example.webgistest.style;

import com.example.webgistest.exception.ErrorException;
import com.example.webgistest.pojo.MapStyle;
import com.example.webgistest.style.styleattribute.RuleWrapper;
import com.example.webgistest.style.styleattribute.StyleWrapper;
import com.example.webgistest.style.stylevariable.GeoType;
import com.example.webgistest.style.stylevariable.GetStyleValue;
import com.example.webgistest.style.stylevariable.StyleChange;
import com.example.webgistest.style.utils.SLDUtil;
import com.example.webgistest.style.utils.StyleUtil;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;

import java.util.ArrayList;
import java.util.List;

public class StyleDetail {

    private StyleWrapper styleWrapper;
    private List<RuleWrapper> ruleWrapperList;

    public StyleDetail(String sld) {
        Style style = null;
        try {
            style = SLDUtil.XMLtoStyle(sld);
        } catch (ErrorException e) {
            e.printStackTrace();
        }
        assert style != null;
        this.styleWrapper = new StyleWrapper(style);
        this.ruleWrapperList = styleWrapper.getAllRule();
    }

    /**
     * 修改样式
     *
     * @param mapStyles
     * @author wnm
     * @date 2021/7/11
     */
    public void changeStyles(List<MapStyle> mapStyles) throws ErrorException {
        if (mapStyles != null && mapStyles.size() > 0) {
            String type = mapStyles.get(0).getGeomType();
            GeoType geoType = GeoType.getType(type);
            for (int i = 0; i < mapStyles.size(); i++) {
                StyleChange change;
                if (ruleWrapperList.size() > i) {
                    change = new StyleChange(ruleWrapperList.get(i));
                    change.changStyle(mapStyles.get(i));
                } else {
                    styleWrapper.addRule(getRule(geoType));
                    ruleWrapperList = styleWrapper.getAllRule();
                    change = new StyleChange(ruleWrapperList.get(i));
                    change.changStyle(mapStyles.get(i));
                }
            }
        }
    }

    /**
     * 获取样式参数
     *
     * @author wnm
     * @date 2021/7/11
     */
    public List<MapStyle> getStyleValue() throws ErrorException {
        List<MapStyle> mapStyles = new ArrayList<>();
        if (ruleWrapperList != null) {
            for (RuleWrapper rule : ruleWrapperList) {
                GetStyleValue styleValue = new GetStyleValue(rule);
                mapStyles.add(styleValue.getStyleValue());
            }
        }
        return mapStyles;
    }

    public Rule getRule(GeoType type) {
        switch (type) {
            case POINT:
                return StyleUtil.createDefaultPointRule();
            case LINE:
                return StyleUtil.createDefaultLineRule();
            case POLYGON:
                return StyleUtil.createDefaultPolygonRule();
            default:
                return null;
        }
    }

    public String getSld() throws ErrorException {
        return StyleUtil.styleToString(styleWrapper.getStyle());
    }

    public void setStyleWrapper(StyleWrapper styleWrapper) {
        this.styleWrapper = styleWrapper;
    }

    public void setRuleWrapperList(List<RuleWrapper> ruleWrapperList) {
        this.ruleWrapperList = ruleWrapperList;
    }

    public StyleWrapper getStyleWrapper() {
        return styleWrapper;
    }

    public List<RuleWrapper> getRuleWrapperList() {
        return ruleWrapperList;
    }

}
