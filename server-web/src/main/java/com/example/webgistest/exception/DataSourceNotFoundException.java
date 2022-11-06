package com.example.webgistest.exception;

/**
 * 矢量数据源不存在错误
 */
public class DataSourceNotFoundException extends Exception {
    public DataSourceNotFoundException(String datasourceName) {
        super(String.format("栅格数据源：%s不存在", datasourceName));
    }
}
