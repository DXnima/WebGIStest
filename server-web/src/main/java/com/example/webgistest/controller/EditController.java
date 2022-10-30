package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.service.IEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * SourceController
 *
 * @author wnm
 * @date 2020/9/20
 */
@Controller
@RequestMapping("edit")
public class EditController {
    @Autowired
    IEditService iEditService;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    ServerResponse<List<Map<String,Object>>> getEdit(String name){
        return iEditService.getEdit(name);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Integer> addEdit(String name, String geom) {
        return iEditService.addEdit(name, geom);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Integer> updateEdit(int id, String name, String geom) {
        return iEditService.updateEdit(id, name, geom);
    }
}
