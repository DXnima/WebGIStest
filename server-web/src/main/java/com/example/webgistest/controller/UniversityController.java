package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.University;
import com.example.webgistest.service.IUniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * SourceController
 *
 * @author wnm
 * @date 2020/9/20
 */
@Controller
@RequestMapping("university")
public class UniversityController {
    @Autowired
    IUniversityService iUniversityService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<University>> getUniversity(String name, String level, String province, String type) {
        return iUniversityService.getUniversity(name,level,province,type);
    }
}
