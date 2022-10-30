package com.example.webgistest.test;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

import java.io.*;
import java.nio.charset.Charset;

public class ReadShapefile {

    public static void main(String[] args) throws IOException{
        String path = "data/shp/capital.shp";
        File file = new File(path);
        ShapefileDataStore shpDataStore = new ShapefileDataStore(file.toURL());
        //设置编码
        Charset charset = Charset.forName("GBK");
        shpDataStore.setCharset(charset);
        String typeName = shpDataStore.getTypeNames()[0];

        SimpleFeatureSource featureSource = shpDataStore.getFeatureSource(typeName);
        SimpleFeatureCollection result = featureSource.getFeatures();
        SimpleFeatureIterator itertor = result.features();
        while (itertor.hasNext()) {
            SimpleFeature feature = itertor.next();
            System.out.println(feature.getDefaultGeometry());
            System.out.println(feature.getAttribute("the_geom").toString());
        }
        itertor.close();
    }
}
