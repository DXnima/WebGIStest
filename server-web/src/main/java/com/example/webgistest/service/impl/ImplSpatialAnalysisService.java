package com.example.webgistest.service.impl;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.service.ISpatialAnalysisService;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ImplSpatialAnalysisService implements ISpatialAnalysisService {

    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING));
    private static final WKTReader reader = new WKTReader(geometryFactory);
    private static final WKTWriter write = new WKTWriter();

    // 空间关系
    public ServerResponse<Map<String, Object>> SpaceRelation(String geom1, String geom2) {
        try {
            Map<String, Object> map = new HashMap<>();
            Geometry geometry1 = reader.read(geom1);
            Geometry geometry2 = reader.read(geom2);
            System.out.println("--------空间关系--------");
            map.put("是否相等", geometry1.equals(geometry2));
            map.put("是否脱节", geometry1.disjoint(geometry2));
            map.put("是否相交", geometry1.intersects(geometry2));
            map.put("是否接触", geometry1.touches(geometry2));
            map.put("是否交叉", geometry1.crosses(geometry2));
            map.put("是否内含", geometry1.within(geometry2));
            map.put("是否包含", geometry1.contains(geometry2));
            map.put("是否重叠", geometry1.overlaps(geometry2));
            return ServerResponse.createBySuccess("空间关系计算成功", map);
        } catch (ParseException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    // 叠加分析
    public ServerResponse<String> InterAnalysis(String geom1, String geom2) {
        try {
            Geometry geometry1 = reader.read(geom1);
            Geometry geometry2 = reader.read(geom2);
            System.out.println("--------叠加分析--------");
            Geometry geometry = geometry1.intersection(geometry2);
            return ServerResponse.createBySuccess("叠加分析计算成功", geometry.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    // 合并分析
    public ServerResponse<String> UnionAnalysis(String geom1, String geom2) {
        try {
            Geometry geometry1 = reader.read(geom1);
            Geometry geometry2 = reader.read(geom2);
            System.out.println("--------合并分析--------");
            Geometry geometry = geometry1.union(geometry2);
            return ServerResponse.createBySuccess("合并分析计算成功", geometry.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    // 差异分析
    public ServerResponse<String> DiffAnalysis(String geom1, String geom2) {
        try {
            Geometry geometry1 = reader.read(geom1);
            Geometry geometry2 = reader.read(geom2);
            System.out.println("--------差异分析--------");
            Geometry geometry = geometry1.difference(geometry2);
            return ServerResponse.createBySuccess("差异分析计算成功", geometry.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    // Sym差异分析
    public ServerResponse<String> SymDiffAnalysis(String geom1, String geom2) {
        try {
            Geometry geometry1 = reader.read(geom1);
            Geometry geometry2 = reader.read(geom2);
            System.out.println("--------sym差异分析--------");
            Geometry geometry = geometry1.symDifference(geometry2);
            return ServerResponse.createBySuccess("sym差异分析计算成功", geometry.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    // 缓冲区分析
    public ServerResponse<String> BuffAnalysis(String geom, double distance) {
        try {
            Geometry geometry = reader.read(geom);
            System.out.println("--------缓冲区分析--------");
            geometry = geometry.buffer(distance);
            return ServerResponse.createBySuccess("缓冲区分析计算成功", geometry.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }
}
