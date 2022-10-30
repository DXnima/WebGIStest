package com.example.webgistest.dao;

import com.example.webgistest.pojo.Edit;

import java.util.List;
import java.util.Map;

public interface EditMapper {

    List<Map<String,Object>> getEdit(String name);

    int addEdit(String name, String geom);

    int updateEdit(int id,String name, String geom);
}
