package com.example.webgistest.style.stylevariable;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;

public enum GeoType {
    POINT,
    MULTIPOINT,
    LINE,
    LINESTRING,
    MULTILINESTRING,
    POLYGON,
    MULTIPOLYGON,
    GEOMETRY;

    /**
     * 不区分大小写字符获取枚举值
     *
     * @param name
     * @return StyleEventType
     * @author wnm
     * @date 2021/6/25
     */
    public static GeoType getType(String name) {
        String val = StringUtils.trimToEmpty(name).toUpperCase();
        Optional<GeoType> possible = Enums.getIfPresent(GeoType.class, val);
        if (!possible.isPresent()) {
            throw new IllegalArgumentException(val + "? 没有这个变量!");
        }
        switch (possible.get()) {
            case POINT:
            case MULTIPOINT:
                return POINT;
            case LINE:
            case LINESTRING:
            case MULTILINESTRING:
                return LINE;
            case POLYGON:
            case MULTIPOLYGON:
                return POLYGON;
            default:
                return GEOMETRY;
        }
    }

    /**
     * 不区分大小写字符获取枚举值
     *
     * @param name
     * @return StyleEventType
     * @author wnm
     * @date 2021/6/25
     */
    public static String getTypeGeom(String name) {
        String val = StringUtils.trimToEmpty(name).toUpperCase();
        Optional<GeoType> possible = Enums.getIfPresent(GeoType.class, val);
        if (!possible.isPresent()) {
            throw new IllegalArgumentException(val + "? 没有这个变量!");
        }
        switch (possible.get()) {
            case POINT:
                return "Point";
            case MULTIPOINT:
                return "MultiPoint";
            case LINE:
                return "Line";
            case LINESTRING:
                return "LineString";
            case MULTILINESTRING:
                return "MultiLineString";
            case POLYGON:
                return "Polygon";
            case MULTIPOLYGON:
                return "MultiPolygon";
            default:
                return "GeomeType";
        }
    }

}
