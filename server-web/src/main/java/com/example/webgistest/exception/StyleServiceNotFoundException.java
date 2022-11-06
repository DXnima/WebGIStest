package com.example.webgistest.exception;

/*
 * 样式服务不存在
 * */
public class StyleServiceNotFoundException extends Exception {
    public StyleServiceNotFoundException(String styleName) {
        super(String.format("样式服务：%s不存在", styleName));
    }
}
