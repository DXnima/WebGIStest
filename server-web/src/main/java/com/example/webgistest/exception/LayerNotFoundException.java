package com.example.webgistest.exception;

/*
 * 图层不存在
 * */
public class LayerNotFoundException extends Exception {
    public LayerNotFoundException(String coverageStoreName) {
        super(String.format("图层：%s不存在", coverageStoreName));
    }
}
