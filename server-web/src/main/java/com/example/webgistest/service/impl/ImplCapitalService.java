package com.example.webgistest.service.impl;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.dao.CapitalMapper;
import com.example.webgistest.pojo.Capital;
import com.example.webgistest.service.ICapitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ImplSourceService
 *
 * @author wnm
 * @date 2020/9/20
 */
@Service
public class ImplCapitalService implements ICapitalService {
    @Autowired
    CapitalMapper capitalMapper;

    //插入
    @Override
    public ServerResponse<Integer> insert(Capital capital){
        int list = capitalMapper.insert(capital);
        if (list == 0) {
            return ServerResponse.createByErrorMessage("插入失败！");
        }
        return ServerResponse.createBySuccess("插入成功",list);
    }

    @Override
    public ServerResponse<List<Capital>> selectAll(){
        List<Capital> list = capitalMapper.selectAll();
        if (list == null) {
            return ServerResponse.createByErrorMessage("无数据！");
        }
        return ServerResponse.createBySuccess("所有数据",list);
    }
}
