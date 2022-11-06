package com.example.webgistest.style.stylevariable;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 * The differnt types of events that might occurr.
 */
public enum StyleEventType {
    GEOMTYPE,
    // general
    NAME,
    PATH,
    MARKNAME,
    SIZE,
    ROTATION,
    OFFSET,
    MINSCALE,
    MAXSCALE,
    // border
    BORDERENABLE,
    BORDERWIDTH,
    BORDERCOLOR,
    BORDEROPACITY,
    // fill
    FILLENABLE,
    FILLCOLOR,
    FILLOPACITY,
    // graphics path
    GRAPHICSPATHBORDER,
    GRAPHICSPATHFILL,
    // wellknown marks for borders and fill, which also need to have width and color
    WKMGRAPHICSBORDER,
    WKMGRAPHICSFILL,
    // dashes and line properties
    DASH,
    DASHOFFSET,
    LINECAP,
    LINEJOIN,
    LINEEND,
    LINESTART,
    // text
    LABELENABLE,
    LABEL,
    LABELFONT,
    LABELCOLOR,
    LABELOPACITY,
    LABELHALOCOLOR,
    LABELHALORADIUS,
    LABELANCHOR,
    LABELDISPLACEMENT,
    LABELROTATION,
    LABELINITIALGAP,
    LABELPERPENDICULAROFFSET,
    LABELMAXDISPLACEMENT_VO,
    LABELREPEAT_VO,
    LABELAUTOWRAP_VO,
    LABELSPACEAROUND_VO,
    LABELFOLLOWLINE_VO,
    LABELMAXANGLEDELTA_VO,
    //Filters
    FILTER;

    /**
     * 不区分大小写字符获取枚举值
     *
     * @param name
     * @return StyleEventType
     * @author wnm
     * @date 2021/6/25
     */
    public static StyleEventType getStyleEventType(String name) {
        String val = StringUtils.trimToEmpty(name).toUpperCase();
        Optional<StyleEventType> possible = Enums.getIfPresent(StyleEventType.class, val);
        if (!possible.isPresent()) {
            throw new IllegalArgumentException(val + "? 没有这个变量!");
        }
        return possible.get();
    }

}
