package com.example.webgistest.service;

import com.example.webgistest.common.ServerResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IWMSService {

    ServerResponse<String> getMapContent(Map paras, HttpServletResponse response);
}
