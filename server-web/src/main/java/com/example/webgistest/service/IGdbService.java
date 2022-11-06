package com.example.webgistest.service;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.GdbLayerInfo;

import java.util.List;
import java.util.Map;

public interface IGdbService {

    //获取图层信息
    public ServerResponse<List<GdbLayerInfo>> getLayers();

    //计算治理长度和河岸治理长度
    public ServerResponse<List<Map>> getResult();

}
