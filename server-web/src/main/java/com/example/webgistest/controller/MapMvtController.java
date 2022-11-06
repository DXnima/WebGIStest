package com.example.webgistest.controller;

import com.example.webgistest.pojo.MapMvt;
import com.example.webgistest.service.IMapMvtService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 矢量瓦片
 */
@ApiSort(value = 8)
@Api(tags = "8.PostGIS生成矢量瓦片")
@Controller
@RequestMapping("mvt_test")
public class MapMvtController {

    @Autowired
    IMapMvtService IMapMvtService;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "1.获取矢量切片", notes = "获取矢量切片.")
    @RequestMapping(value = "/{z}/{x}/{y}.pbf", method = RequestMethod.GET, produces = "application/x-protobuf")
    @ResponseBody
    public Object selectMVT(@PathVariable("z") int z, @PathVariable("x") int x, @PathVariable("y") int y, HttpServletResponse response){
        MapMvt mapMvt = IMapMvtService.selectMVT(z,x,y);
        //设置缓存有效时间为十分钟
//        response.setHeader("Cache-Control", "max-age=600");
        //response.setHeader("Cache-Result", mapMvt.getCacheCode());
        return mapMvt.getMvt();
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "2.缓存矢量切片", notes = "缓存矢量切片.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startZoom", value = "开始zoom", required = true, dataTypeClass = int.class),
            @ApiImplicitParam(name = "endZoom", value = "终止zoom", required = true, dataTypeClass = int.class),
            @ApiImplicitParam(name = "bbox", value = "bbox边界", required = true, dataTypeClass = double[].class)
    })
    @RequestMapping(value = "/cache", method = RequestMethod.GET)
    @ResponseBody
    public void cacheMVT(int startZoom,int endZoom,double[] bbox){
        IMapMvtService.cacheMVT(startZoom, endZoom, bbox);
    }

}
