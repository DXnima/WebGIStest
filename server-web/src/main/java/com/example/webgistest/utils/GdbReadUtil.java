package com.example.webgistest.utils;

import com.example.webgistest.pojo.GdbFeatureInfo;
import com.example.webgistest.pojo.GdbLayerInfo;
import org.gdal.gdal.gdal;
import org.gdal.ogr.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GdbReadUtil {

    /*
        gdal初始化
      	1、载入c++动态库
      	2、注册gdal驱动
     */
    static {
    	System.out.println("---GdalHolder--gdal初始化");
		loadLibrary();
		registerAll();
    }

    public static void loadLibrary() {
        System.out.println("---GdalLibrary--gdal载入动态库");
        try {
            //根据系统环境加载资源
            String systemType = System.getProperty("os.name");
            String file="";
            boolean isWin= systemType.toLowerCase().contains("win");
            if(isWin) {
                file="/lib/win/gdalalljni.dll";
            }
            else {
                file="/lib/linux/libgdalalljni.so";
            }
            //从资源文件加载动态库
            GdalLibraryUtil.loadFromResource(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerAll() {
        System.out.println("---GdalHolder--gdal注册所有的驱动--开始");
        // 注册所有的驱动
        ogr.RegisterAll();
        //支持中文路径
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");
        //支持中文字段
        gdal.SetConfigOption("SHAPE_ENCODING","CP936");
        System.out.println("---GdalHolder--gdal注册所有的驱动--结束");
    }

    //获取每个图层信息
    public static List<GdbLayerInfo> getLayerInfo(String FirePath) {
        Driver driver = ogr.GetDriverByName("OpenFileGDB");
        if (driver == null) {
            return null;
        }
        List<GdbLayerInfo> listLayer = new ArrayList<>();
        DataSource dataSource = null;
        try {
            dataSource = driver.Open(FirePath, 0);
            int num = dataSource.GetLayerCount();
            for (int i = 0; i < num; i++) {
                //用于存储属性信息
                List<Map> attributes = new ArrayList<>();
                //用于存储要素信息
                List<GdbFeatureInfo> features = new ArrayList<>();
                //获取图层
                Layer layer = dataSource.GetLayer(i);
                //图层名称名称
                String layerName = layer.GetName();
                //获取坐标系EPSG代码
                String proj = layer.GetSpatialRef().GetAuthorityCode(null);
                // 获取图层要数个数
                long count = layer.GetFeatureCount();
                if (0 != count) {
                    do {//获取图层下的要素
                        GdbFeatureInfo gdbFeatureInfo = new GdbFeatureInfo();
                        Feature feature = layer.GetNextFeature();
                        if (null == feature){
                            break;
                        }
                        //获取要素ID
                        gdbFeatureInfo.setId(feature.GetFID());
                        //获取Geometry
                        Geometry geometry = feature.GetGeometryRef();
                        if (geometry != null) {
                            String geometryJson = geometry.ExportToJson();
                            //获取要素Geometry
                            gdbFeatureInfo.setGeometry(geometryJson);
                        }
                        //获取属性信息
                        Map attribute = new HashMap();
                        for (int p = 0; p < feature.GetFieldCount(); p++) {
                            attribute.put(feature.GetFieldDefnRef(p).GetName(), getProperty(feature, p));
                        }
                        //获取要素属性信息
                        gdbFeatureInfo.setProperties(attribute);
                        features.add(gdbFeatureInfo);
                        attributes.add(attribute);
                        feature.delete();
                    } while (true);
                }
                GdbLayerInfo gdbLayerInfo = new GdbLayerInfo();
                gdbLayerInfo.setName(layerName);
                gdbLayerInfo.setProj(proj);
                gdbLayerInfo.setCount(count);
                gdbLayerInfo.setFeatures(features);
                listLayer.add(gdbLayerInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dataSource != null) {
                dataSource.delete();
            }
        }
        return listLayer;
    }

    private static Object getProperty(Feature feature, int index) {
        int type = feature.GetFieldType(index);
        PropertyGetter propertyGetter;
        if (type < 0 || type >= propertyGetters.length) {
            propertyGetter = stringPropertyGetter;
        } else {
            propertyGetter = propertyGetters[type];
        }
        try {
            return propertyGetter.get(feature, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 属性获取器
     */
    @FunctionalInterface
    private interface PropertyGetter {
        Object get(Feature feature, int index);
    }

    private static final PropertyGetter stringPropertyGetter = (feature, index) -> feature.GetFieldAsString(index);

    /**
     * feature.GetFieldType(index)得到一个属性类型的int值,该值对应具体类型
     */
    private static final PropertyGetter[] propertyGetters = new PropertyGetter[]{
            (feature, index) -> feature.GetFieldAsInteger(index),//0	Integer
            (feature, index) -> feature.GetFieldAsIntegerList(index),//1	IntegerList
            (feature, index) -> feature.GetFieldAsDouble(index),//2	Real
            (feature, index) -> feature.GetFieldAsDoubleList(index),//3	RealList
            stringPropertyGetter,//4	String
            (feature, index) -> feature.GetFieldAsStringList(index),//5	StringList
            stringPropertyGetter,//6	(unknown)
            stringPropertyGetter,//7	(unknown)
            (feature, index) -> feature.GetFieldAsBinary(index),//8	Binary
            (feature, index) -> {
                int[] pnYear = new int[1];
                int[] pnMonth = new int[1];
                int[] pnDay = new int[1];
                int[] pnHour = new int[1];
                int[] pnMinute = new int[1];
                float[] pfSecond = new float[1];
                int[] pnTZFlag = new int[1];
                feature.GetFieldAsDateTime(index, pnYear, pnMonth, pnDay, pnHour, pnMinute, pfSecond, pnTZFlag);
                java.sql.Date date = java.sql.Date.valueOf(LocalDate.of(pnYear[0], pnMonth[0], pnDay[0]));
                return date;
            },//9	Date
            (feature, index) -> {
                int[] pnYear = new int[1];
                int[] pnMonth = new int[1];
                int[] pnDay = new int[1];
                int[] pnHour = new int[1];
                int[] pnMinute = new int[1];
                float[] pfSecond = new float[1];
                int[] pnTZFlag = new int[1];
                feature.GetFieldAsDateTime(index, pnYear, pnMonth, pnDay, pnHour, pnMinute, pfSecond, pnTZFlag);
                float fSecond = pfSecond[0];
                int s = (int) fSecond;
                int ns = (int) (1000000000 * fSecond - s);
                Time time = Time.valueOf(LocalTime.of(pnHour[0], pnMinute[0], s, ns));
                return time;
            },// 10	Time
            (feature, index) -> {
                int[] pnYear = new int[1];
                int[] pnMonth = new int[1];
                int[] pnDay = new int[1];
                int[] pnHour = new int[1];
                int[] pnMinute = new int[1];
                float[] pfSecond = new float[1];
                int[] pnTZFlag = new int[1];
                feature.GetFieldAsDateTime(index, pnYear, pnMonth, pnDay, pnHour, pnMinute, pfSecond, pnTZFlag);
                float fSecond = pfSecond[0];
                int s = (int) fSecond;
                int ns = (int) (1000000000 * fSecond - s);
                LocalDateTime localDateTime = LocalDateTime.of(
                        LocalDate.of(pnYear[0], pnMonth[0], pnDay[0]),
                        LocalTime.of(pnHour[0], pnMinute[0], s, ns)
                );
                Timestamp timestamp = Timestamp.valueOf(localDateTime);
                return timestamp;
            },//11	DateTime
            (feature, index) -> feature.GetFieldAsInteger64(index),//12	Integer64
            (feature, index) -> feature.GetFieldAsIntegerList(index),//13 Integer64List
            //>=14	(unknown)
    };
}

