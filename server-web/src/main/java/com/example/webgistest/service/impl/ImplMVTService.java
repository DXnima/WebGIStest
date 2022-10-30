package com.example.webgistest.service.impl;

import com.example.webgistest.dao.MVTMapper;
import com.example.webgistest.pojo.MVT;
import com.example.webgistest.service.IMVTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImplMVTService implements IMVTService {

    @Autowired
    private MVTMapper mvtMapper;

    @Override
    public MVT getMvt(int z, int x, int y){
        return mvtMapper.getMvt(z, x, y);
    }
}
