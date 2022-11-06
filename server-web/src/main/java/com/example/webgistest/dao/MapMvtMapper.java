package com.example.webgistest.dao;

import com.example.webgistest.pojo.MapMvt;
import org.springframework.stereotype.Component;

@Component
public interface MapMvtMapper {
    MapMvt selectMVT(int z, int x, int y);

    Object getBBoxXY(int z, double minX, double minY, double maxX, double maxY);

}