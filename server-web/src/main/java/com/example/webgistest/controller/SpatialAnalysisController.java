package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.service.ISpatialAnalysisService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * SourceController
 * 空间分析
 * @author wnm
 * @date 2020/9/20
 */
@ApiSort(value = 11)
@Api(tags = "11.空间分析接口")
@Controller
@RequestMapping("spa")
public class SpatialAnalysisController {

    @Autowired
    ISpatialAnalysisService iSpatialAnalysisService;

    // 空间关系
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "1.空间关系", notes = "geom1和geom2的空间关系.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "geom1", value = "WKT类型的Geometry", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "geom2", value = "WKT类型的Geometry", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/relation", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Map<String, Object>> SpaceRelation(String geom1, String geom2){
        return iSpatialAnalysisService.SpaceRelation(geom1, geom2);
    }

    // 叠加分析
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "2.叠加分析", notes = "geom1和geom2的叠加分析结果.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "geom1", value = "WKT类型的Geometry", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "geom2", value = "WKT类型的Geometry", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/inter", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> InterAnalysis(String geom1, String geom2){
        return iSpatialAnalysisService.InterAnalysis(geom1, geom2);
    }

    // 合并分析
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "3.合并分析", notes = "geom1和geom2的合并分析结果.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "geom1", value = "WKT类型的Geometry", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "geom2", value = "WKT类型的Geometry", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/union", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> UnionAnalysis(String geom1, String geom2){
        return iSpatialAnalysisService.UnionAnalysis(geom1, geom2);
    }

    // 差异分析
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "4.差异分析", notes = "geom1和geom2的差异分析结果.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "geom1", value = "WKT类型的Geometry", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "geom2", value = "WKT类型的Geometry", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/diff", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> DiffAnalysis(String geom1, String geom2){
        return iSpatialAnalysisService.DiffAnalysis(geom1, geom2);
    }

    // Sym差异分析
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "5.Sym差异分析", notes = "geom1和geom2的Sym差异分析结果.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "geom1", value = "WKT类型的Geometry", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "geom2", value = "WKT类型的Geometry", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/symdiff", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> SymDiffAnalysis(String geom1, String geom2){
        return iSpatialAnalysisService.SymDiffAnalysis(geom1, geom2);
    }

    // 缓冲区分析
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "6.缓冲区分析", notes = "根据geometry设置缓冲区半径获取缓冲区.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "geom", value = "WKT类型的Geometry", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "distance", value = "缓冲区半径", required = true, dataTypeClass = double.class)
    })
    @RequestMapping(value = "/buff", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> BuffAnalysis(String geom, double distance){
        return iSpatialAnalysisService.BuffAnalysis(geom, distance);
    }
}
