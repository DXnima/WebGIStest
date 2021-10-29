package com.example.webgistest.controller;

import com.example.webgistest.pojo.MVT;
import com.example.webgistest.service.IMVTService;
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
@Controller
@RequestMapping("mvt")
public class MVTController {

    @Autowired
    IMVTService iMvtService;

    @RequestMapping(value = "/{z}/{x}/{y}.pbf", method = RequestMethod.GET, produces = "application/x-protobuf")
    @ResponseBody
    public Object selectMVT(@PathVariable("z") int z, @PathVariable("x") int x, @PathVariable("y") int y, HttpServletResponse response){
        MVT mvt = iMvtService.getMvt(z,x,y);
        //设置缓存有效时间为十分钟
        //response.setHeader("Cache-Control", "max-age=600");
        return mvt.getMvt();
    }

}
