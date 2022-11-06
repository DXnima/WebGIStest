package com.example.webgistest.exception;

/*
 * 图层组不存在
 * */
public class LayerGroupNotFoundException extends Exception {
    public LayerGroupNotFoundException(String layerGroupName) {
        super(String.format("图层组：%s不存在", layerGroupName));
    }
}
