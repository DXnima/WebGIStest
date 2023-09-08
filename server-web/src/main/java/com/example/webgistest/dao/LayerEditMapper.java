package com.example.webgistest.dao;

import java.util.List;
import java.util.Map;

public interface LayerEditMapper {

    List<Map<String,Object>> getEdit(String name);

    int addEdit(String name, String geom);

    int updateEdit(int id,String name, String geom);
}
