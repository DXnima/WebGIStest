package com.example.webgistest.test;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

public class SpatialRelation {
    //private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
    private static GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING));
    private static WKTReader reader = new WKTReader(geometryFactory);
    private static WKTWriter write = new WKTWriter();

    public static void main(String[] args){
        String wktPoint = "POINT(99.55456549674278 36.18574580318503)";
        String wktLine = "LINESTRING(108.32803893589 41.306670233001,99.950999898452 25.84722546391)";
        String wktPolygon = "POLYGON((100.02715479879 32.168082192159,102.76873121104 37.194305614622,107.0334056301 34.909658604412,105.96723702534 30.949603786713,100.02715479879 32.168082192159))";
        String wktPolygon1 = "POLYGON((96.219409781775 32.777321394882,96.219409781775 40.240501628236,104.82491352023001 40.240501628236,104.82491352023001 32.777321394882,96.219409781775 32.777321394882))";

        try {
            Point point = (Point) reader.read(wktPoint);
            LineString line = (LineString) reader.read(wktLine);
            Polygon polygon = (Polygon) reader.read(wktPolygon);
            Polygon polygon1 = (Polygon) reader.read(wktPolygon1);

            Geometry intersection = polygon1.symDifference( polygon );
            System.out.println(write.write(intersection));

            System.out.println("--------空间关系--------");
            System.out.println(polygon.contains(point));
            System.out.println(polygon.intersects(line));
            System.out.println(polygon.overlaps(polygon1));

            System.out.println("--------空间计算--------");
            Geometry intersec=polygon.intersection(polygon1);
            Geometry union=polygon.union(polygon1);
            Geometry difference=polygon.difference(polygon1);
            System.out.println("--------叠加分析--------");
            System.out.println(write.write(intersec));
            System.out.println("--------合并分析--------");
            System.out.println(write.write(union));
            System.out.println("--------差异分析--------");
            System.out.println(write.write(difference));

        } catch (ParseException e){
            e.printStackTrace();
        }
    }
}
