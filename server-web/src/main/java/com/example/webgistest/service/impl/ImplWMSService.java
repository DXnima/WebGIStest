package com.example.webgistest.service.impl;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.service.IWMSService;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.styling.SLDParser;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

@Service
public class ImplWMSService implements IWMSService {

    private static MapContent map = null;

    public void openFile() {
        try {
            ImageIO.scanForPlugins();
            String shpPath = "", sldPath = "";
            String p = System.getProperty("user.dir");
            shpPath = "data/capital/capital.shp";
            sldPath = "data/capital/capital.sld";
            map = new MapContent();
            addShapeLayer(shpPath, sldPath);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void addShapeLayer(String shpPath, String sldPath){
        try {
            File file = new File(shpPath);
            ShapefileDataStore shpDataStore = null;
            shpDataStore = new ShapefileDataStore(file.toURL());
            //设置编码
            Charset charset = Charset.forName("GBK");
            shpDataStore.setCharset(charset);
            String typeName = shpDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = null;
            featureSource = shpDataStore.getFeatureSource(typeName);

            Style style = SLD.createSimpleStyle(featureSource.getSchema());
            if (!sldPath.equals("")) {
                //SLD的方式
                File sldFile = new File(sldPath);
                StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
                SLDParser stylereader = new SLDParser(styleFactory, sldFile.toURI().toURL());
                Style[] stylearray = stylereader.readXML();
                style = stylearray[0];
            } else {
                SLD.setPolyColour(style, Color.RED);
            }

            Layer layer = new FeatureLayer(featureSource, style);
            map.addLayer(layer);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public ServerResponse<String> getMapContent(Map paras, HttpServletResponse response){
        openFile();
        try{
            double[] bbox = (double[]) paras.get("bbox");
            double x1 = bbox[0], y1 = bbox[1],
                    x2 = bbox[2], y2 = bbox[3];
            int width = (Integer) paras.get("width"),
                    height=(Integer) paras.get("height");

            // 设置输出范围
            CoordinateReferenceSystem crs = CRS.decode(paras.get("srs").toString());
            ReferencedEnvelope mapArea = new ReferencedEnvelope(x1, x2, y1, y2, crs);
            // 初始化渲染器
            StreamingRenderer sr = new StreamingRenderer();
            sr.setMapContent(map);
            // 初始化输出图像
            BufferedImage bi = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.getGraphics();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Rectangle rect = new Rectangle(0, 0, width, height);
            // 绘制地图
            sr.paint((Graphics2D) g, rect, mapArea);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean flag = ImageIO.write(bi, "png", out);
            byte[] wmsByte = out.toByteArray();

            OutputStream os = response.getOutputStream();
            InputStream is = new ByteArrayInputStream(wmsByte);
            try {
                int count = 0;
                byte[] buffer = new byte[1024 * 1024];
                while ((count = is.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                }
                os.flush();
                return ServerResponse.createBySuccess("WMS服务发布成功！","true");
            }
            catch (IOException e) {
                e.printStackTrace();
                return ServerResponse.createBySuccess("WMS服务发布失败！",e.toString());
            }
            finally {
                os.close();
                is.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return ServerResponse.createBySuccess("WMS服务发布失败！",e.toString());
        }
    }

}
