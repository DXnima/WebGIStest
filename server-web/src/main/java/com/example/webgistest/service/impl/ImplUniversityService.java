package com.example.webgistest.service.impl;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.dao.UniversityMapper;
import com.example.webgistest.pojo.University;
import com.example.webgistest.service.IUniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImplUniversityService  implements IUniversityService {
    @Autowired
    UniversityMapper universityMapper;

    public ServerResponse<List<University>> getUniversity(String name, String level, String province, String type){
        List<University> list = universityMapper.getUniversity(name,level,province,type);
        if (list == null) {
            return ServerResponse.createByErrorMessage("无数据！");
        }
        return ServerResponse.createBySuccess("所有大学数据",list);
    }
}
