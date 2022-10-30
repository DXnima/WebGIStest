package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.Capital;
import com.example.webgistest.service.ICapitalService;
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
@RequestMapping("capital")
public class CapitalController {

    @Autowired
    ICapitalService iCapitalService;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Integer> insert(Capital capital) {
        return iCapitalService.insert(capital);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Capital>> allSources() {
        return iCapitalService.selectAll();
    }
}
