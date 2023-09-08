package com.example.webgistest.service;

import com.example.webgistest.common.ServerResponse;

import java.util.List;
import java.util.Map;

public interface ILayerEditService {

    ServerResponse<List<Map<String,Object>>> getEdit(String name);

    ServerResponse<Integer> addEdit(String name, String geom);

    ServerResponse<Integer> updateEdit(int id, String name, String geom);

}
