package com.example.webgistest.geoserver;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;

@ApiModel(value = "PostGIS配置类")
public class ImprovePostGISDatastore {

    @ApiModelProperty(value = "PostGIS主机IP", example = "localhost")
    private String host;
    @ApiModelProperty(value = "PostGIS端口", example = "5432")
    private int port = 5432;
    @ApiModelProperty(value = "PostGIS用户名", example = "postgres")
    private String user = "postgres";
    @ApiModelProperty(value = "PostGIS密码", example = "123456")
    private String password;
    @ApiModelProperty(value = "PostGIS数据库")
    private String database;

    public ImprovePostGISDatastore(String host, String password, String database) {
        this.host = host;
        this.password = password;
        this.database = database;
    }

    public ImprovePostGISDatastore(String host, int port, String user, String password, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public ImprovePostGISDatastore() {
        super();
    }

    /**
     * 构建 GSPostGISDatastoreEncoder 对象，并将其返回
     *
     * @return GSPostGISDatastoreEncoder 对象
     */
    public GSPostGISDatastoreEncoder builder() {
        GSPostGISDatastoreEncoder build = new GSPostGISDatastoreEncoder(getDatabase());
        build.setHost(getHost());
        build.setPort(getPort());
        build.setUser(getUser());
        build.setPassword(getPassword());
        build.setDatabase(getDatabase());
        build.setExposePrimaryKeys(true);

        return build;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}

