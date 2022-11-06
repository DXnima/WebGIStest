package com.example.webgistest.service;

import com.example.webgistest.pojo.MapMvt;

public interface IMapMvtService {
    MapMvt selectMVT(int z, int x, int y);

    void cacheMVT(int startZoom,int endZoom,double[] bbox);

}
