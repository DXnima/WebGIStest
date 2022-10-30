package com.example.webgistest.test;

import com.example.webgistest.utils.FilterUtil;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.styling.Font;
import org.geotools.styling.Stroke;
import org.geotools.styling.*;
import org.geotools.xml.styling.SLDTransformer;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

import javax.xml.transform.TransformerException;
import java.awt.*;

public class geotoolsCreateSLD {
    //默认常量
    private static final Color LINE_COLOUR = Color.BLUE;//边界颜色
    private static final Color FILL_COLOUR = Color.CYAN;//填充色
    private static final float OPACITY = 1.0f;//透明度
    private static final float LINE_WIDTH = 1.0f;//线宽
    private static final float POINT_SIZE = 10.0f;//点大小
    //编辑的常量
    private static final String THE_GEOM="the_geom";//属性名
    private static final String RULE_NAME="rule_name";//规则名
    private static final String STYLE_NAME="style_name";//样式名称
    private static final String SLD_NAME="sld_name";//SLD名字
    private static final String SLD_TITLE="sld_title";//SLD标题

    private static final StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private static final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    private static final SLDTransformer transformer = new SLDTransformer();  //导出文本用

    public static void point() throws Exception{
        //生成一个FeatureTypeStyle：
        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        //添加边线，加入到FeatureTypeStyle
        Stroke stroke = sf.createStroke(ff.literal(LINE_COLOUR),ff.literal(LINE_WIDTH),ff.literal(OPACITY));
        //添加填充颜色，加入到FeatureTypeStyle
        Fill fill = sf.createFill(ff.literal(FILL_COLOUR),ff.literal(OPACITY));
        //创建Mark，点样式对象
        Mark mark = sf.getSquareMark();
        //点边线样式赋值
        mark.setStroke(stroke);
        //点填充样式赋值
        mark.setFill(fill);
        //点形状赋值为：circle
        mark.setWellKnownName(ff.literal("circle"));
        //设置图标
        ExternalGraphic eg = sf.createExternalGraphic("https://gitee.com/dxwangnima/images/raw/master/img/20210612155110.png","image/png");
        //创建图画
        Graphic graphic = sf.createDefaultGraphic();
        //清除
        graphic.graphicalSymbols().clear();
        //添加设置的点
        graphic.graphicalSymbols().add(mark);
        //添加设置的图标
        graphic.graphicalSymbols().add(eg);
        //设置点大小
        graphic.setSize(ff.literal(POINT_SIZE));
        //创建特征对象
        Symbolizer symbolizer = sf.createPointSymbolizer(graphic,null);
        /**
         *几种过滤条件
         * 1.使用feature id 作为过滤条件
         * Filter filt = ff.id(featureIds);
         * 2.相等，不等，超过，不超过几种情况作为过滤条件
         * Filter left = ff.equals(ff.property( "NAME" ), ff.literal( "武汉" ));
         * 3.CQL "ATTR1 < 10 AND ATTR2 < 2 OR ATTR3 > 10"
         * Filter filter= CQL.toFilter("NAME like '%湖%'");
         * @author wnm
         * @date 2021/6/11
         * @param []
         * @return void
         */
        //CQL作为过滤条件
        Filter filter= CQL.toFilter("name = '武汉'");
        //解决字段名为中文的情况
        FilterUtil.decodeFilter(filter,"name"); //用真正的中文字段名fieldName来替换临时字段名name
        //创建规则
        Rule rule = sf.createRule();
        //规则名称
        rule.setName(RULE_NAME);
        //设置过滤条件
        //rule.setFilter(filter);
        //添加样式
        rule.symbolizers().add(symbolizer);
        //FeatureTypeStyle添加规则，一组样式一个规则
        fts.rules().add(rule);
        //生成style，style对应UserStyle标签：
        Style style = sf.createStyle();
        //设置样式名称
        style.setName("capital");
        //样式添加FeatureTypeStyle
        style.featureTypeStyles().add(fts);
        //生成StyledLayerDescriptor，顶级sld
        StyledLayerDescriptor sld = new StyledLayerDescriptorImpl();
        //创建图层
        UserLayer userLayer = new UserLayerImpl();
        //所有样式传入数组
        userLayer.setUserStyles(style);
        sld.addStyledLayer(userLayer);
        try {
            transformer.setIndentation(4);
            //打印
            System.out.println(transformer.transform(sld));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void linestring(){
        //生成一个FeatureTypeStyle：
        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        //对于polygon
        //添加填充颜色、和边线，加入到FeatureTypeStyle：
        Stroke stroke = sf.createStroke(ff.literal(LINE_COLOUR),ff.literal(LINE_WIDTH),ff.literal(OPACITY));
        Symbolizer symbolizer = sf.createLineSymbolizer(stroke, "the_geom");
        Rule rule = sf.createRule();
        rule.setName(RULE_NAME);
        rule.symbolizers().add(symbolizer);
        fts.rules().add(rule);
        //生成style，style对应UserStyle标签：
        Style style = sf.createStyle();
        style.setName(STYLE_NAME);
        style.featureTypeStyles().add(fts);
        //生成StyledLayerDescriptor，顶级sld
        StyledLayerDescriptorImpl sld = new StyledLayerDescriptorImpl();
        UserLayer userLayer = new UserLayerImpl();
        userLayer.setUserStyles(style);  //传入数组
        sld.addStyledLayer(userLayer);
        try {
            transformer.setIndentation(4);
            System.out.println(transformer.transform(sld));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void polygon() throws Exception{
        //生成一个FeatureTypeStyle：
        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        //对于polygon
        //添加填充颜色、和边线，加入到FeatureTypeStyle：
        Stroke stroke = sf.createStroke(ff.literal(LINE_COLOUR),ff.literal(LINE_WIDTH),ff.literal(OPACITY));
        Fill fill = sf.createFill(ff.literal(FILL_COLOUR),ff.literal(OPACITY));
        Symbolizer symbolizer = sf.createPolygonSymbolizer(stroke, fill, THE_GEOM);
        Rule rule = sf.createRule();
        rule.setName(RULE_NAME);
        rule.symbolizers().add(symbolizer);
        fts.rules().add(rule);
        //生成style，style对应UserStyle标签：
        Style style = sf.createStyle();
        style.setName(STYLE_NAME);
        style.featureTypeStyles().add(fts);
        //生成StyledLayerDescriptor，顶级sld
        StyledLayerDescriptorImpl sld = new StyledLayerDescriptorImpl();
        UserLayer userLayer = new UserLayerImpl();
        userLayer.setUserStyles(style);  //传入数组
        sld.setName(SLD_NAME);
        sld.setTitle(SLD_TITLE);
        sld.addStyledLayer(userLayer);
        try {
            transformer.setIndentation(4);
            System.out.println(transformer.transform(sld));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void text(){
        //生成一个FeatureTypeStyle：
        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        //首先创建TextSymbolizer：
        TextSymbolizer textSymbolizer = sf.createTextSymbolizer();
        //设置字体样式：
        Fill textFill = sf.createFill(ff.literal(Color.decode("#0551F5")),ff.literal(0.5));  //颜色 不透明度
        Font font = sf.createFont(ff.literal("宋体"),ff.literal("Regular"),ff.literal("normal"),ff.literal(22));
        //字体边缘：
        Halo halo = sf.createHalo(sf.createFill(ff.literal(Color.decode("#F0F408"))),ff.literal(5));
        //注记文字可以设置字段属性和固定文本：
        Expression exp = ff.literal("中国");  //显示固定文本
        // Expression exp = ff.property("Name");  //Name字段
        //设置注记锚点、偏移和旋转：
        AnchorPoint anchorPoint = sf.createAnchorPoint(ff.literal(0.0),ff.literal(0.0));
        Displacement displacement = sf.createDisplacement(ff.literal(0.0),ff.literal(0.0));
        Expression rotation = ff.literal(-45);  //旋转-45度
        LabelPlacement placement = sf.createPointPlacement(anchorPoint,displacement,rotation);  //这里没用sf.createLinePlacement(...)
        //将信息加入textSymbolizer：
        textSymbolizer.setFont(font);
        textSymbolizer.setFill(textFill);
        textSymbolizer.setLabel(exp);
        textSymbolizer.setHalo(halo);
        textSymbolizer.setLabelPlacement(placement);
        //最后将textSymbolizer添加到Rule：
        Rule rule = sf.createRule();
        rule.symbolizers().add(textSymbolizer);  //规则rule
        rule.setName(RULE_NAME);
        rule.symbolizers().add(textSymbolizer);
        fts.rules().add(rule);
        //生成style，style对应UserStyle标签：
        Style style = sf.createStyle();
        style.setName(STYLE_NAME);
        style.featureTypeStyles().add(fts);
        //生成StyledLayerDescriptor，顶级sld
        StyledLayerDescriptorImpl sld = new StyledLayerDescriptorImpl();
        UserLayer userLayer = new UserLayerImpl();
        userLayer.setUserStyles(style);  //传入数组
        sld.addStyledLayer(userLayer);
        try {
            transformer.setIndentation(4);
            System.out.println(transformer.transform(sld));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        System.out.println("点要素SLD样式\n");
        point();
        System.out.println("线要素SLD样式\n");
        linestring();
        System.out.println("面要素SLD样式\n");
        polygon();
        System.out.println("文本标签SLD样式\n");
        text();
    }

}
