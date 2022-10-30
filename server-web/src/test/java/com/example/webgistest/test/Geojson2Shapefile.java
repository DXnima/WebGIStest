package com.example.webgistest.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.webgistest.utils.FileUtil;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Geojson2Shapefile {

    public static void main(String[] args) throws IOException{
        double start = System.currentTimeMillis();

        FileUtil fileUtil = new FileUtil();
        String path = "data/geo2shp/capital.geojson",
            outPath = "data/geo2shp/capital.shp";
        System.out.println(path);
        String strGeojson = fileUtil.getFileContent(path);
        JSONObject jsonObject = JSON.parseObject(strGeojson);
        JSONArray jsonArray = jsonObject.getJSONArray("features");
        GeometryJSON gjson = new GeometryJSON();
        FeatureJSON fjson = new FeatureJSON();

        //创建shape文件对象
        File file = new File(outPath);
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put( ShapefileDataStoreFactory.URLP.key, file.toURI().toURL() );
        ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
        //定义图形信息和属性信息
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setCRS(DefaultGeographicCRS.WGS84);
        tb.setName("shapefile");
        tb.add("the_geom", Point.class);
        tb.add("name", String.class);
        ds.createSchema(tb.buildFeatureType());
        //设置编码
        Charset charset = Charset.forName("GBK");
        ds.setCharset(charset);
        //设置Writer
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);

        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonFeature = jsonArray.getJSONObject(i);
            Reader reader = new StringReader(jsonFeature.toJSONString());
            SimpleFeature _feature = fjson.readFeature(reader);
            SimpleFeature feature = writer.next();
            feature.setAttribute("the_geom", _feature.getDefaultGeometry());
            feature.setAttribute("name", _feature.getAttribute("name"));
            writer.write();
        }
        writer.close();
        ds.dispose();

        double end = System.currentTimeMillis();
        System.out.println("共耗时："+(end-start)+"MS");
    }
}
