package com.example.webgistest.exception;

public class ExistedException extends Exception {
    public ExistedException(String message) {
        super(message + "已存在");
    }
}
