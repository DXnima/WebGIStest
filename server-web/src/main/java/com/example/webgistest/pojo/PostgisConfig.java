package com.example.webgistest.pojo;

/**
 * geoserver rest接口
 * 连接Postgis数据库，发布地图
 * 配置类
 */
public class PostgisConfig {
    //postgis连接配置
    private String host;
    private int port;//端口号
    private String user;//用户名
    private String password;//用户密码
    private String database;//数据库名称
    private String workspace;//待创建和发布图层的工作区名称workspace
    private String store; //待创建和发布图层的数据存储名称store
    private String table; // 数据库要发布的表名称,后面图层名称和表名保持一致
    private String srs; //坐标系

    public PostgisConfig(String host, int port, String user, String password, String database, String workspace, String store, String table, String srs) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
        this.workspace = workspace;
        this.store = store;
        this.table = table;
        this.srs = srs;
    }

    public PostgisConfig() {
        super();
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

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getSrs() {
        return srs;
    }

    public void setSrs(String srs) {
        this.srs = srs;
    }
}
