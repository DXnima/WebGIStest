package com.example.webgistest.common;

public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    Error(1, "ERROR"),
    UnRegist(2, "Need_REGIST"),
    UnLogin(10, "Need_LOGIN");
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
