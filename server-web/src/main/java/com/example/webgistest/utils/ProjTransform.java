package com.example.webgistest.utils;

/**
 * @author DXnima
 * 提供了百度坐标（BD09）、国测局坐标（火星坐标，GCJ02）、和WGS84坐标系之间的转换
 * 命名规则：
 *    1、bd代表百度的坐标，gcj代表国测局火星坐标，wgs代表wgs84坐标
 */
public class ProjTransform {
    /**
     * 定义一些常量
     */
    private final double x_PI = 3.14159265358979324 * 3000.0 / 180.0;
    private final double PI = 3.1415926535897932384626;
    private final double a = 6378245.0;
    private final double ee = 0.00669342162296594323;

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     * @param bd_lon
     * @param bd_lat
     * @returns {*[]}
     */
    public double[] bd09togcj02(double bd_lon, double bd_lat){
        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_PI);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new double[]{gg_lon, gg_lat};
    }
    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * 即谷歌、高德 转 百度
     * @param gcj_lon
     * @param gcj_lat
     * @returns {*[]}
     */
    public double[] gcj02tobd09(double gcj_lon, double gcj_lat){
        double z = Math.sqrt(gcj_lon * gcj_lon + gcj_lat * gcj_lat) + 0.00002 * Math.sin(gcj_lat * x_PI);
        double theta = Math.atan2(gcj_lat, gcj_lon) + 0.000003 * Math.cos(gcj_lon * x_PI);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new double[]{bd_lon, bd_lat};
    }
    /**
     * WGS84转GCj02
     * @param wgs_lon
     * @param wgs_lat
     * @returns {*[]}
     */
    public double[] wgs84togcj02(double wgs_lon, double wgs_lat){
        if (out_of_china(wgs_lon, wgs_lat)) {
            return new double[]{wgs_lon, wgs_lat};
        } else {
            double dlat = transformlat(wgs_lon - 105.0, wgs_lat - 35.0);
            double dlon = transformlon(wgs_lon - 105.0, wgs_lat - 35.0);
            double radlat = wgs_lat / 180.0 * PI;
            double magic = Math.sin(radlat);
            magic = 1 - ee * magic * magic;
            double sqrtmagic = Math.sqrt(magic);
            dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
            dlon = (dlon * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
            double mglat = wgs_lat + dlat;
            double mglon = wgs_lon + dlon;
            return new double[]{mglon, mglat};
        }
    }
    /**
     * GCJ02 转换为 WGS84
     * @param gcj_lon
     * @param gcj_lat
     * @returns {*[]}
     */
    public double[] gcj02towgs84(double gcj_lon, double gcj_lat){
        if (out_of_china(gcj_lon, gcj_lat)) {
            return new double[]{gcj_lon, gcj_lat};
        } else {
            double dlat = transformlat(gcj_lon - 105.0, gcj_lat - 35.0);
            double dlng = transformlon(gcj_lon - 105.0, gcj_lat - 35.0);
            double radlat = gcj_lat / 180.0 * PI;
            double magic = Math.sin(radlat);
            magic = 1 - ee * magic * magic;
            double sqrtmagic = Math.sqrt(magic);
            dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
            dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
            double mglat = gcj_lat + dlat;
            double mglng = gcj_lon + dlng;
            return new double[]{gcj_lon * 2 - mglng, gcj_lat * 2 - mglat};
        }
    }
    /**
     * 百度转换为wgs84
     * @param bd_lon
     * @param bd_lat
     * @return
     */
    public double[] bd09towgs84(double bd_lon, double bd_lat){
        //1、bd09->gcj
        double[] bd09_gcj02 = bd09togcj02(bd_lon, bd_lat);
        //2、gcj->wgs84
        return gcj02towgs84(bd09_gcj02[0], bd09_gcj02[1]);
    }
    /**
     * wgs84z转换为百度坐标
     * @param wgs_lon
     * @param wgs_lat
     * @return
     */
    public double[] wgs84tobd09(double wgs_lon, double wgs_lat){
        //1、wgs84->gcj
        double[] wgs84_gcj02 = wgs84togcj02(wgs_lon, wgs_lat);
        //2、gcj->bd09
        return gcj02tobd09(wgs84_gcj02[0], wgs84_gcj02[1]);
    }

    /**
     * 判断是否中国境内
     * @param lon
     * @param lat
     * @return
     */
    private boolean out_of_china(double lon, double lat){
        // 纬度3.86~53.55,经度73.66~135.05
        return !(lon > 73.66 && lon < 135.05 && lat > 3.86 && lat < 53.55);
    }
    /**
     * 经度转换
     * @param lon
     * @param lat
     * @return
     */
    private double transformlon(double lon, double lat){
        double ret = 300.0 + lon + 2.0 * lat + 0.1 * lon * lon + 0.1 * lon * lat + 0.1 * Math.sqrt(Math.abs(lon));
        ret += (20.0 * Math.sin(6.0 * lon * PI) + 20.0 * Math.sin(2.0 * lon * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lon * PI) + 40.0 * Math.sin(lon / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lon / 12.0 * PI) + 300.0 * Math.sin(lon / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }
    /**
     * 纬度转换
     * @param lon
     * @param lat
     * @return
     */
    private double transformlat(double lon, double lat){
        double ret = -100.0 + 2.0 * lon + 3.0 * lat + 0.2 * lat * lat + 0.1 * lon * lat + 0.2 * Math.sqrt(Math.abs(lon));
        ret += (20.0 * Math.sin(6.0 * lon * PI) + 20.0 * Math.sin(2.0 * lon * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }


    public static void main(String[] args){
        ProjTransform proj = new ProjTransform();
        double[] result = proj.wgs84togcj02(115.9656134,39.80327155);
        System.out.println(result[0]+", "+result[1]);
    }
}
