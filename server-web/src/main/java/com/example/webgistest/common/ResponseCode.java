package com.example.webgistest.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Response统一返回状态码")
public enum ResponseCode {
    @ApiModelProperty(value = "成功状态码")
    SUCCESS(200, "SUCCESS"),
    @ApiModelProperty(value = "失败状态码")
    Error(0, "ERROR"),
    @ApiModelProperty(value = "Geoserver配置失败状态码")
    UNCONFIG(10, "请配置Geoserver!");

    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
