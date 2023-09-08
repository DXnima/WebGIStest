package com.example.webgistest.exception;

import com.example.webgistest.common.ServerResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: weather
 * @ClassName GlobalExceptionHandler
 * 全局异常捕获统一返回
 * @description:
 * @author: wnm
 * @create: 2020-03-25 13:49
 * @Version 1.0
 **/

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    /**
     * 异常捕获
     *
     * @param e 这里捕获所有异常， 我们也可以再写一个方法，单独捕获自定义异常
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ServerResponse handlerException(Exception e) {
        String msg;
        if (e.getMessage() == null) {
            msg = String.valueOf(e);
        } else {
            msg = e.getMessage();
        }
        return ServerResponse.createByErrorMessage("错误！" + msg);
    }
}
