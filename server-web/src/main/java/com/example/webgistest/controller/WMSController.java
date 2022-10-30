package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.service.IWMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * SourceController
 *
 * @author wnm
 * @date 2020/9/20
 */
@Controller
@RequestMapping("geoserver")
public class WMSController {

    @Autowired
    IWMSService iWMSService;

    @RequestMapping("/wms")
    public ServerResponse<String> getMap(@RequestParam("BBOX")String bbox,
                                         @RequestParam("WIDTH")String width,
                                         @RequestParam("HEIGHT")String height,
                                         @RequestParam("LAYERS")String layer,
                                         @RequestParam("SRS")String srs,
                                         HttpServletResponse response) {
        int _w = Integer.parseInt(width),
                _h = Integer.parseInt(height);

        String[] BBOXS = bbox.split(",");
        double[] _bbox = new double[]{
                Double.parseDouble(BBOXS[0]),
                Double.parseDouble(BBOXS[1]),
                Double.parseDouble(BBOXS[2]),
                Double.parseDouble(BBOXS[3])
        };
        Map paras = new HashMap();
        paras.put("bbox", _bbox);
        paras.put("srs", srs);
        paras.put("width", _w);
        paras.put("height", _h);
        return iWMSService.getMapContent(paras, response);
    }
}
