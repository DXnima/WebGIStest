package com.example.webgistest.service;

import com.example.webgistest.pojo.MVT;

public interface IMVTService {
    MVT getMvt(int z, int x, int y);
}
