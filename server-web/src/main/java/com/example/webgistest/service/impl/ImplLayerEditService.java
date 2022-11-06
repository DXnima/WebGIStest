package com.example.webgistest.service.impl;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.dao.LayerEditMapper;
import com.example.webgistest.service.ILayerEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ImplSourceService
 *
 * @author wnm
 * @date 2020/9/20
 */
@Service
public class ImplLayerEditService implements ILayerEditService {
    @Autowired
    LayerEditMapper layerEditMapper;

    public ServerResponse<List<Map<String,Object>>> getEdit(String name){
        List<Map<String,Object>> list= layerEditMapper.getEdit(name);
        if (list.size() == 0) {
            return ServerResponse.createByErrorMessage("数据为空！");
        }
        return ServerResponse.createBySuccess("查询成功！",list);
    }

    public ServerResponse<Integer> addEdit(String name, String geom){
        int list = layerEditMapper.addEdit(name, geom);
        if (list == 0) {
            return ServerResponse.createByErrorMessage("插入失败！");
        }
        return ServerResponse.createBySuccess("插入成功",list);
    }

    public ServerResponse<Integer> updateEdit(int id, String name, String geom){
        int list = layerEditMapper.updateEdit(id, name, geom);
        if (list == 0) {
            return ServerResponse.createByErrorMessage("修改失败！");
        }
        return ServerResponse.createBySuccess("修改成功",list);
    }
}
