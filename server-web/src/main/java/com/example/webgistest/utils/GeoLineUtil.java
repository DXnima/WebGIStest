package com.example.webgistest.utils;

import org.locationtech.jts.algorithm.distance.DistanceToPoint;
import org.locationtech.jts.algorithm.distance.PointPairDistance;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.linearref.LinearLocation;
import org.locationtech.jts.linearref.LocationIndexedLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 线要素处理工具类
 */
public class GeoLineUtil {

    /**
     * 获取点到线段最近的点
     *               ●                      point
     *               |
     *               |
     * --------------●--------------        line
     * @param point 点
     * @param geom 线要素
     * @return 点
     */
    public static Coordinate getMinPoint(Coordinate point, Geometry geom) {
        PointPairDistance ppd = new PointPairDistance();
        DistanceToPoint.computeDistance(geom, point, ppd);
        return ppd.getCoordinates()[0];
    }

    /**
     * 获取点到直线最近的距离
     * @param point 点
     * @param geom  直线
     * @return   距离
     */
    public static double getMinDistance(Coordinate point, Geometry geom) {
        PointPairDistance ppd = new PointPairDistance();
        DistanceToPoint.computeDistance(geom, point, ppd);
        return ppd.getDistance();
    }

    /**
     * 截取两点之间的线段
     *            start               end
     * -------------●------------------●-------------           line
     * @param start 起点
     * @param end 终点
     * @param line 线要素
     * @return 线要素
     */
    public static Geometry clipLine(Coordinate start, Coordinate end, Geometry line){
        //位置索引线
        LocationIndexedLine lil = new LocationIndexedLine(line);
        //设置起始
        LinearLocation startL = lil.indexOf(start);
        //设置终止
        LinearLocation endL = lil.indexOf(end);
        //获取截取的线段
        Geometry result = lil.extractLine(startL, endL);
        return result;
    }

    /**
     * 根据点裁剪线段
     *                  point
     * ------------------●-------------------     line
     * @param point 点
     * @param line 线要素
     * @return 多条线
     */
    public static List<Geometry> clipLineByPoint(Coordinate point, Geometry line){
        List<Geometry> list = new ArrayList<>();
        //获取第一条线上所有点的个数
        int pLen = line.getCoordinates().length;
        //获取线起点
        Coordinate startP = line.getCoordinates()[0];
        //获取线终点
        Coordinate endP = line.getCoordinates()[pLen - 1];
        list.add(clipLine(startP, point, line));
        list.add(clipLine(point, endP, line));
        return list;
    }

    /**
     * 一条线line1到另一条线line2上的映射
     *            ----------                  line1
     *            |        |
     *            |        |
     * --------------------------------       line2
     *
     *            ----------                  reslut
     * @param line1 线1
     * @param line2 线2
     * @return  然后1在2上的映射
     */
    public static Geometry getLine(Geometry line1,Geometry line2) {
        Geometry result = null;
        String[] types = {"MultiLineString", "LineString"};
        //判断是否为线类型
        if (Arrays.asList(types).contains(line1.getGeometryType()) &&
                Arrays.asList(types).contains(line1.getGeometryType())) {
            //获取第一条线上所有点的个数
            int pLen = line1.getCoordinates().length;
            //获取线起点
            Coordinate startP = line1.getCoordinates()[0];
            //获取线终点
            Coordinate endP = line1.getCoordinates()[pLen - 1];
            //获取线中点
            Coordinate centerP = line1.getCoordinates()[pLen/2 -1];
            //获取点到线上最近的点
            Coordinate minP = getMinPoint(centerP, line2);
            //获取截取的线段
            result = clipLine(startP, endP, line2);
            //判断中心点是否在当前截取的线段上
            if (getMinDistance(minP, result) > 0.0001){
                //不在， 则以中心点分两段进行截取
                List<Geometry> list = clipLineByPoint(minP, line2);
                if (list.size() == 2) {
                    result = clipLine(startP, centerP, list.get(0)); //截取起点-中点这段
                    result = result.union(clipLine(centerP, endP, list.get(1)));//截取中点-终点这段
                }
            }
        }
        return result;
    }

}
