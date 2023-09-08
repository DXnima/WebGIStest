package com.example.webgistest.service.impl;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.GdbFeatureInfo;
import com.example.webgistest.pojo.GdbLayerInfo;
import com.example.webgistest.service.IGdbService;
import com.example.webgistest.utils.GeoLineUtil;
import com.example.webgistest.utils.GdbReadUtil;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImplGdbService implements IGdbService {

    private static List<GdbLayerInfo> LIST = new ArrayList<>();

    public ImplGdbService(){
        LIST = GdbReadUtil.getLayerInfo("data/testdata.gdb");
    }

    @Override
    public ServerResponse<List<GdbLayerInfo>> getLayers(){
        if (LIST.size() == 0) {
            return ServerResponse.createByErrorMessage("图层获取失败！");
        }
        return ServerResponse.createBySuccess("图层获取成功！",LIST);
    }

    @Override
    public ServerResponse<List<Map>> getResult() {
        List<Map> list = new ArrayList<>();
        GdbLayerInfo zlgcLayer = new GdbLayerInfo(); //治理工程图层
        GdbLayerInfo xzqhLayer = new GdbLayerInfo(); //行政区划图层
        GdbLayerInfo haLayer = new GdbLayerInfo();   //河岸图层
        for (GdbLayerInfo layer : LIST) {
            switch (layer.getName()) {
                case "治理工程":
                    zlgcLayer = layer;
                    break;
                case "行政区划":
                    xzqhLayer = layer;
                    break;
                case "河岸":
                    haLayer = layer;
                    break;
            }
        }
        GeometryJSON gjson = new GeometryJSON();
        if (haLayer.getFeatures().size() == 1) {
            Reader reader = new StringReader(haLayer.getFeatures().get(0).getGeometry());
            try {
                //河岸线
                Geometry haGeom = gjson.read(reader);
                for (GdbFeatureInfo xzqhFeature : xzqhLayer.getFeatures()) {
                    Map map = new HashMap();
                    //获取每一个要素的名称
                    String name = xzqhFeature.getProperties().get("ADDVNM").toString();
                    map.put("name", name);
                    gjson = new GeometryJSON();
                    reader = new StringReader(xzqhFeature.getGeometry());
                    //行政区划转为geometry
                    Geometry xzqGeom = gjson.read(reader);
                    //获取行政区划内的河岸线
                    Geometry haGeom_xzqh = haGeom.intersection(xzqGeom);
                    //存储治理线与河岸线映射
                    Geometry zlhaGeom = null;
                    //记录治理工程量
                    double zlgcLen = 0;
                    //记录治理河岸长度
                    double zlhaLen = 0;
                    for (GdbFeatureInfo zlgcFeature : zlgcLayer.getFeatures()) {
                        gjson = new GeometryJSON();
                        reader = new StringReader(zlgcFeature.getGeometry());
                        //治理工程线要素
                        Geometry zlgcGeom = gjson.read(reader);
                        //治理线到河岸线上的映射，防止重叠，进行合并
                        if (zlhaGeom == null) {
                            zlhaGeom = GeoLineUtil.getLine(zlgcGeom, haGeom_xzqh);
                        } else {
                            zlhaGeom = zlhaGeom.union(GeoLineUtil.getLine(zlgcGeom, haGeom_xzqh));
                        }
                        //行政区域内是否包含工程治理线
                        if (xzqGeom.intersects(zlgcGeom)) {
                            //行政区域是否与工程治理线是否接触边缘
                            if (xzqGeom.crosses(zlgcGeom)) {
                                //接触边缘则计算在行政区内的线段再求长度
                                zlgcGeom = zlgcGeom.intersection(xzqGeom);
                            }
                            //计算长度
                            zlgcLen += zlgcGeom.getLength();
                        }
                    }
                    if (zlhaGeom != null){
                        //裁剪指定行政区内的河岸治理线
                        zlhaGeom = zlhaGeom.intersection(xzqGeom);
                        zlhaLen += zlhaGeom.getLength();
                    }
                    map.put("zlgc", zlgcLen / 1000);
                    map.put("ha", zlhaLen / 1000);
                    list.add(map);
                }
            } catch (Exception e) {
                return ServerResponse.createBySuccessMessage("图层分析失败！\n"+ e.toString());
            }
        }
        if (list.size() == 0) {
            return ServerResponse.createByErrorMessage("图层分析失败！");
        }
        return ServerResponse.createBySuccess("图层分析成功！",list);
    }

}
