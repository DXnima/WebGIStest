package com.example.webgistest.test;

import com.example.webgistest.utils.FileUtil;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

public class Shapefile2Geojson {

    public static void main(String[] args) throws IOException{
        double start = System.currentTimeMillis();
        String path = "data/shp/capital.shp",
            outPath = "data/shp/capital.geojson";
        File file = new File(path);
        ShapefileDataStore shpDataStore = new ShapefileDataStore(file.toURL());
        //设置编码
        Charset charset = Charset.forName("GBK");
        shpDataStore.setCharset(charset);
        String typeName = shpDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = shpDataStore.getFeatureSource(typeName);
        SimpleFeatureCollection result = featureSource.getFeatures();
        SimpleFeatureIterator itertor = result.features();

        StringBuffer sb = new StringBuffer();
        sb.append("{\"type\": \"FeatureCollection\",\"features\": [");

        FeatureJSON fjson = new FeatureJSON();
        int size = result.size(),
            i = 0;
        while (itertor.hasNext()) {
            SimpleFeature feature = itertor.next();
            StringWriter writer = new StringWriter();
            fjson.writeFeature(feature, writer);
            sb.append(writer.toString());
            if(i!=size) sb.append(",");
            i++;
        }

        itertor.close();
        sb.append("]}");

        FileUtil fileUtil = new FileUtil();
        fileUtil.append2File(outPath, sb.toString());

        double end = System.currentTimeMillis();
        System.out.println("共耗时："+(end-start)+"MS");
    }
}
