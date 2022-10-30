package com.example.webgistest.dao;

import com.example.webgistest.pojo.Capital;

import java.util.List;

public interface CapitalMapper {

    int insert(Capital record);

    List<Capital> selectAll();
}