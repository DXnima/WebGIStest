package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.service.IWMSService;
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

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * SourceController
 * 发布wms服务
 * @author wnm
 * @date 2020/9/20
 */
@ApiSort(value = 12)
@Api(tags = "12.Geotools发布WMS服务")
@Controller
@RequestMapping("geotools")
public class WMSController {

    @Autowired
    IWMSService iWMSService;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "wms服务", notes = "获取WMS地图服务.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bbox", value = "范围", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "width", value = "图片高度", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "height", value = "图片宽度", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "layer", value = "图层", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "srs", value = "EPSC坐标系代码", required = true, dataTypeClass = String.class)
    })
    @RequestMapping(value = "/wms", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> getMap(String bbox, String width, String height, String layer, String srs, HttpServletResponse response) {
        int _w = Integer.parseInt(width),
                _h = Integer.parseInt(height);

        String[] BBOXS = bbox.split(",");
        double[] _bbox = new double[]{
                Double.parseDouble(BBOXS[0]),
                Double.parseDouble(BBOXS[1]),
                Double.parseDouble(BBOXS[2]),
                Double.parseDouble(BBOXS[3])
        };
        Map<String,Object> paras = new HashMap<>();
        paras.put("bbox", _bbox);
        paras.put("srs", srs);
        paras.put("width", _w);
        paras.put("height", _h);
        return iWMSService.getMapContent(paras, response);
    }
}
