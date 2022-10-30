package com.example.webgistest.pojo;

/**
 * geoserver rest接口
 * 发布Shapefile文件
 * 配置类
 */
public class ShapefileConfig {
    private String workspace;     //待创建和发布图层的工作区名称workspace
    private String store; //待创建和发布图层的数据存储名称store
    private String srs; //坐标系
    private String zipFile;//压缩文件的完整路径
    private String layername;//图层名称
    private String urlDatastore; //shp文件所在的位置

    public ShapefileConfig(String workspace, String store, String srs, String zipFile, String layername, String urlDatastore) {
        this.workspace = workspace;
        this.store = store;
        this.srs = srs;
        this.zipFile = zipFile;
        this.layername = layername;
        this.urlDatastore = urlDatastore;
    }

    public ShapefileConfig() {
        super();
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

    public String getSrs() {
        return srs;
    }

    public void setSrs(String srs) {
        this.srs = srs;
    }

    public String getZipFile() {
        return zipFile;
    }

    public void setZipFile(String zipFile) {
        this.zipFile = zipFile;
    }

    public String getLayername() {
        return layername;
    }

    public void setLayername(String layername) {
        this.layername = layername;
    }

    public String getUrlDatastore() {
        return urlDatastore;
    }

    public void setUrlDatastore(String urlDatastore) {
        this.urlDatastore = urlDatastore;
    }
}
