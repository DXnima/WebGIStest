package com.example.webgistest.service.impl;

import com.example.webgistest.dao.MapMvtMapper;
import com.example.webgistest.pojo.MapMvt;
import com.example.webgistest.service.IMapMvtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImplMapMVTService implements IMapMvtService {

    @Autowired
    private MapMvtMapper mapMvtMapper;

    /**
     * 查询矢量切片
     */
    @Override
    public MapMvt selectMVT(int z, int x, int y) {
        return mapMvtMapper.selectMVT(z, x, y);
    }

    /**
     * 缓存矢量切片
     */
    @Override
    public void cacheMVT(int startZoom,int endZoom,double[] bbox){
        Object startBBOX = mapMvtMapper.getBBoxXY(startZoom,bbox[0],bbox[1],bbox[2],bbox[3]);
        Object endBBOX = mapMvtMapper.getBBoxXY(startZoom,bbox[0],bbox[1],bbox[2],bbox[3]);
    }
}