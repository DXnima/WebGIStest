package com.example.webgistest.exception;

import com.example.webgistest.common.ServerResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpErrorController
 * 统一异常处理
 *
 * @author wnm
 * @date 2020/4/15
 */
@RestController
public class HttpErrorController implements ErrorController {

    private final static String ERROR_PATH = "/error";

    @ResponseBody
    @RequestMapping(path = ERROR_PATH)
    public ServerResponse error(HttpServletRequest request, HttpServletResponse response) {
        String msg;
        switch (response.getStatus()) {
            case 400:
                msg = "请求的数据格式不符!";
                break;
            case 401:
                msg = "请求的数字签名不匹配!";
                break;
            case 404:
                msg = "未找到该资源!";
                break;
            case 500:
                msg = "服务器内部错误!";
                break;
            case 503:
                msg = "服务器正忙，请稍后再试!";
                break;
            default:
                msg = "HTTP错误!";
        }
        return ServerResponse.createByErrorCodeMessage(response.getStatus(), msg);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
