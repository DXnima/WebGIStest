package com.example.webgistest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.webgistest.dao") //扫描的mappeer
@SpringBootApplication
public class WebGIStestApplication {

    public static String port;

    public static String contextPath;


    @Value("${server.port}")
    public void setPort(String port) {
        WebGIStestApplication.port = port;
    }

    @Value("${server.servlet.context-path}")
    public void setContextPath(String contextPath) {
        WebGIStestApplication.contextPath = contextPath;
    }


    private static void printServerUrl() {
        String url = String.format("http://%s%s%s%s", "localhost" + ":", port, contextPath, "doc.html");
        System.err.println("-----------------------------------------------------------------");
        System.err.println("##### WebGIStest程序启动成功！");
        System.err.println("##### 接口URI : " + url);
        System.err.println("-----------------------------------------------------------------");
    }

    public static void main(String[] args) {
        SpringApplication.run(WebGIStestApplication.class, args);
        printServerUrl();
    }

}
