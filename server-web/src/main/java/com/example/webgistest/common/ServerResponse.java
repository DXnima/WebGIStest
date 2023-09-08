package com.example.webgistest.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author: lvwenchao
 * @data: 2019/2/27 9:04
 * @description:复用
 * @Version: 1.0
 **/
@ApiModel(value = "Response统一返回类")
public class ServerResponse<T> implements Serializable {
    @ApiModelProperty(value = "状态码")
    private int status;  //代码
    @ApiModelProperty(value = "返回的数据")
    private T data;
    @ApiModelProperty(value = "文本提示")
    private String msg;

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    @ApiModelProperty(value = "是否成功")
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }


    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.Error.getCode(), ResponseCode.Error.getMsg());
    }


    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new ServerResponse<T>(ResponseCode.Error.getCode(), errorMessage);
    }

    public static <T> ServerResponse<T> createByConfig() {
        return new ServerResponse<T>(ResponseCode.UNCONFIG.getCode(), ResponseCode.UNCONFIG.getMsg());
    }


    public static <T> ServerResponse<T> createByConfigMessage(String errorMessage) {
        return new ServerResponse<T>(ResponseCode.UNCONFIG.getCode(), errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse<T>(errorCode, errorMessage);
    }

}