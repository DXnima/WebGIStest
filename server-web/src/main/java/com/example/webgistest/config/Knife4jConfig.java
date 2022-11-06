package com.example.webgistest.config;

import com.example.webgistest.WebGIStestApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Knife4jConfig
 * Knife4j 配置类
 *
 * @author wnm
 * @date 2020/9/25
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("WebGIStest后端服务接口")
                        .description("# WebGIStest")
                        .termsOfServiceUrl("http://localhost:" + WebGIStestApplication.port)
                        .version("1.0")
                        .contact(new Contact("DXnima", "", "dxnima@qq.com"))
                        .build())
                //分组名称
                .groupName("1.0版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.webgistest.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

}
