package com.example.webgistest.test;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.util.factory.GeoTools;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Geotools查询
 */
public class FilterExam {
    //使用openGIS FilterFactory2
    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
    /**
     * 获取数据
     *
     * @param shpPath
     * @return
     * @throws IOException
     */
    public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureS(
            String shpPath) throws IOException {
        ShapefileDataStore store = new ShapefileDataStore(new File(shpPath)
                .toURI().toURL());
        FeatureSource<SimpleFeatureType, SimpleFeature> features = store
                .getFeatureSource(store.getTypeNames()[0]);
        return features;
    }


    /**
     * 获取feature id 集合
     * @param fs
     * @return
     * @throws IOException
     */
    public Set<FeatureId> getFeatureId(
            FeatureSource<SimpleFeatureType, SimpleFeature> fs) throws IOException {
        FeatureIterator<SimpleFeature> itertor = fs.getFeatures().features();
        Set<FeatureId> fids = new HashSet<FeatureId>();
        while (itertor.hasNext()) {
            SimpleFeature feature = itertor.next();
            fids.add(feature.getIdentifier());
        }
        itertor.close();
        return fids;
    }

    /**
     * 使用feature id 作为过滤条件
     * @param fs
     * @return
     * @throws IOException
     */
    public FeatureCollection filterFidEx(
            FeatureSource<SimpleFeatureType, SimpleFeature> fs)
            throws IOException {
        Set<FeatureId> fids = getFeatureId(fs);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        Filter filt = (Filter) ff.id(fids);
        FeatureCollection col = fs.getFeatures(filt);
        return col;
    }

    /**
     * 相等，不等，超过，不超过几种情况
     *
     * @param fs
     * @return
     * @throws IOException
     */
    public FeatureCollection compareFilterEx(
            FeatureSource<SimpleFeatureType, SimpleFeature> fs)
            throws IOException {
        Filter left = ff.equals(ff.property( "NAME" ), ff.literal( "湖" ));
        FeatureCollection col = fs.getFeatures(left);
        return col;
    }

    /**
     * CQL (common query language)
     * @param fs
     * @param name
     * @return
     * @throws CQLException
     * @throws IOException
     * Filter f = CQL.toFilter("ATTR1 < 10 AND ATTR2 < 2 OR ATTR3 > 10");
    Filter f = CQL.toFilter("NAME = 'New York' ");
    Filter f = CQL.toFilter("NAME LIKE 'New%' ");
    Filter f = CQL.toFilter("NAME IS NULL");
    Filter f = CQL.toFilter("DATE BEFORE 2006-11-30T01:30:00Z");
    Filter f = CQL.toFilter("NAME DOES-NOT-EXIST");
    Filter f = CQL.toFilter("QUANTITY BETWEEN 10 AND 20");
    Filter f = CQL.toFilter("CROSSES(SHAPE, LINESTRING(1 2, 10 15))");
    Filter f = CQL.toFilter("BBOX(SHAPE, 10,20,30,40)");
    Expression e = CQL.toExpression("NAME");
    Expression e = CQL.toExpression("QUANTITY * 2");
    Expression e = CQL.toExpression("strConcat(NAME, 'suffix')");
    List filters = CQL.toFilterList("NAME IS NULL;BBOX(SHAPE, 10,20,30,40);INCLUDE");
     */
    public FeatureCollection filterCQL(FeatureSource<SimpleFeatureType, SimpleFeature> fs, String name) throws CQLException, IOException {
        return fs.getFeatures(CQL.toFilter("NAME like '%" + name + "%'"));
    }


    public static void main(String[] args) throws IOException, CQLException, ParserConfigurationException {
        FilterExam fe = new FilterExam();
        String shpPath = "data/capital/capital.shp";
        FeatureSource<SimpleFeatureType, SimpleFeature> fs = fe.getFeatureS(shpPath);
        FeatureCollection col1 = fe.compareFilterEx(fs);
        System.out.println(col1);
        FeatureCollection col2 = fe.filterFidEx(fs);
        System.out.println(col2.size());
        FeatureCollection col3 = fe.filterCQL(fs, "湖");
        System.out.println(col3);
    }
}
