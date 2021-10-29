package com.example.webgistest.dao;

import com.example.webgistest.pojo.MVT;
import org.springframework.stereotype.Component;

@Component
public interface MVTMapper {
    MVT getMvt(int z, int x, int y);
}