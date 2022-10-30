package com.example.webgistest.test;

import org.locationtech.jts.geom.*;

public class GeomIsType {

    public String getVectorIcon(Class<?> geom) {
        if (Point.class.isAssignableFrom(geom) || MultiPoint.class.isAssignableFrom(geom)) {
            return "POINT";
        } else if (LineString.class.isAssignableFrom(geom)
                || MultiLineString.class.isAssignableFrom(geom)) {
            return "LINE";
        } else if (Polygon.class.isAssignableFrom(geom)
                || MultiPolygon.class.isAssignableFrom(geom)) {
            return "POLYGON";
        } else {
            return "GEOMETRY";
        }
    }

}
