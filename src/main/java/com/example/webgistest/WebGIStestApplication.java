package com.example.webgistest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.webgistest.dao") //扫描的mappeer
@SpringBootApplication
public class WebGIStestApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebGIStestApplication.class, args);
    }

}
