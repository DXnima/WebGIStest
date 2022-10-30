package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.LayerInfo;
import com.example.webgistest.service.IGdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("gdb")
public class GdbController {

    @Autowired
    IGdbService iGdbService;

    /**
     * 获取图层信息
     * @return
     */
    @RequestMapping(value = "/layers", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<LayerInfo>> getLayers(){
        return iGdbService.getLayers();
    }

    /**
     * 计算工程治理长度和河岸治理长度
     * @return
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Map>> getCalculate(){
        String file = "data/testdata.gdb";
        return iGdbService.getResult();
    }
}
