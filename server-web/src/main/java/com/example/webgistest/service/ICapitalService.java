package com.example.webgistest.service;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.Capital;

import java.util.List;

/**
 * ISourceService
 *
 * @author wnm
 * @date 2020/9/20
 */
public interface ICapitalService {

    //插入
    ServerResponse<Integer> insert(Capital capital);

    //查询所有数据
    ServerResponse<List<Capital>> selectAll();


}
