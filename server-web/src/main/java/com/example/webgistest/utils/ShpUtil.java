package com.example.webgistest.utils;

import com.example.webgistest.exception.ErrorException;
import com.example.webgistest.style.stylevariable.GeoType;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.GeometryType;

import java.io.File;
import java.io.IOException;

/**
 * Created by admin on 2017/11/20.
 */
public class ShpUtil {

    /**
     * 获取shp文件的Geometry类型
     *
     * @param pathName 文件名
     * @return GeoType
     * @throws ErrorException
     */
    public static GeoType getShpType(String pathName) throws ErrorException {
        File file = new File(pathName);
        try {
            // 读取到数据存储中
            FileDataStore dataStore = FileDataStoreFinder.getDataStore(file);
            // 获取特征资源
            SimpleFeatureSource simpleFeatureSource = dataStore.getFeatureSource();
            // 要素集合
            SimpleFeatureCollection simpleFeatureCollection = simpleFeatureSource.getFeatures();
            // 获取要素迭代器
            SimpleFeatureIterator featureIterator = simpleFeatureCollection.features();
            if (featureIterator.hasNext()) {
                // 要素对象
                SimpleFeature feature = featureIterator.next();
                // geometry属性
                GeometryAttribute geometryAttribute = feature.getDefaultGeometryProperty();
                // geometry类型
                GeometryType geometryType = geometryAttribute.getType();
                // geometry类型名称
                String name = geometryType.getName().toString();
                return GeoType.getType(name);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrorException("shp文件读取失败！");
        }
    }

}
