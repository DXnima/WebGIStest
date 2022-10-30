package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.service.ISpatialAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("spa")
public class SpatialAnalysisController {

    @Autowired
    ISpatialAnalysisService iSpatialAnalysisService;

    // 空间关系
    @RequestMapping(value = "/relation", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Map<String, Object>> SpaceRelation(String geom1, String geom2){
        return iSpatialAnalysisService.SpaceRelation(geom1, geom2);
    }

    // 叠加分析
    @RequestMapping(value = "/inter", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> InterAnalysis(String geom1, String geom2){
        return iSpatialAnalysisService.InterAnalysis(geom1, geom2);
    }

    // 合并分析
    @RequestMapping(value = "/union", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> UnionAnalysis(String geom1, String geom2){
        return iSpatialAnalysisService.UnionAnalysis(geom1, geom2);
    }

    // 差异分析
    @RequestMapping(value = "/diff", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> DiffAnalysis(String geom1, String geom2){
        return iSpatialAnalysisService.DiffAnalysis(geom1, geom2);
    }

    // Sym差异分析
    @RequestMapping(value = "/symdiff", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> SymDiffAnalysis(String geom1, String geom2){
        return iSpatialAnalysisService.SymDiffAnalysis(geom1, geom2);
    }

    // 缓冲区分析
    @RequestMapping(value = "/buff", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> BuffAnalysis(String geom, double distance){
        return iSpatialAnalysisService.BuffAnalysis(geom, distance);
    }
}
