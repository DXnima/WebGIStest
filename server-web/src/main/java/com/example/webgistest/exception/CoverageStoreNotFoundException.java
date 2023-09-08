package com.example.webgistest.exception;

/*
 * 栅格数据源不存在
 * */
public class CoverageStoreNotFoundException extends Exception {
    public CoverageStoreNotFoundException(String coverageStoreName) {
        super(String.format("矢量数据源：%s不存在", coverageStoreName));
    }
}
