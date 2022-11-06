package com.example.webgistest.geoserver;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;

@ApiModel(value = "Geoserver配置类")
public class ImproveGeoServerPublisher extends GeoServerRESTPublisher {

    @ApiModelProperty(value = "Geoserver服务的链接", example = "http://localhost:8080/geoserver")
    private String restURL;
    @ApiModelProperty(value = "用户名", example = "admin")
    private String username;
    @ApiModelProperty(value = "密码", example = "geoserver")
    private String password;

    public ImproveGeoServerPublisher(String restURL, String username, String password) {
        super(restURL, username, password);

        this.restURL = restURL;
        this.username = username;
        this.password = password;
    }

    public String getRestURL() {
        return restURL;
    }

    public void setRestURL(String restURL) {
        this.restURL = restURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
