package com.example.webgistest.utils;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.decoder.RESTStyle;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSShapefileDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 调用rest接口
 * geoserver发布地图
 */
public class GeoserverPublish {

    /**
     * geoserver rest接口
     * PostGIS发布服务
     * PostGIS配置类
     */
    public static class PostgisConfig {
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

    /**
     * geoserver rest接口
     * 发布Shapefile文件
     * 配置类
     */
    public static class ShapefileConfig {
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

    //发布postgis中的数据
    public static void GeoserverPublishPostGISData(String url, String username, String passwd, PostgisConfig postgisConfig) throws IOException {
        String ws = postgisConfig.getWorkspace();     //待创建和发布图层的工作区名称workspace
        String store_name = postgisConfig.getStore(); //待创建和发布图层的数据存储名称store
        String table_name = postgisConfig.getTable(); // 数据库要发布的表名称,后面图层名称和表名保持一致
        String srs = postgisConfig.getSrs();
        //判断工作区（workspace）是否存在，不存在则创建
        URL u = new URL(url);
        GeoServerRESTManager manager = new GeoServerRESTManager(u, username, passwd);
        String w = GeoserverCreateWorkspaces(manager, ws);
        String s = GeoserverCreateStorePostGIS(manager, postgisConfig, ws, store_name);
        //String st=GeoserverCreateStyle(manager,ws,"","");
        String l = GeoserverCreateLayerPostGIS(manager, ws, store_name, table_name, srs, "point");
        System.err.println(w + s + l);
    }

    //发布shapefile数据
    public static void GeoserverPublishShapefileData(String url, String username, String passwd, ShapefileConfig shapefileConfig) throws IOException {
        String ws = shapefileConfig.getWorkspace();     //待创建和发布图层的工作区名称workspace
        String store_name = shapefileConfig.getStore(); //待创建和发布图层的数据存储名称store
        String srs = shapefileConfig.getSrs();
        String layername = shapefileConfig.getLayername();//图层名称
        //shp文件所在的位置
        String urlDatastore = shapefileConfig.getUrlDatastore();
        //判断工作区（workspace）是否存在，不存在则创建
        URL u = new URL(url);
        //获取管理对象
        GeoServerRESTManager manager = new GeoServerRESTManager(u, username, passwd);
        String w = GeoserverCreateWorkspaces(manager, ws);
        //判断数据存储（datastore）是否已经存在，不存在则创建
        String s = GeoserverCreateStoreShapefile(manager, ws, store_name, urlDatastore);
        //String st=GeoserverCreateStyle(manager,ws,"wnm","");
        //判断图层是否已经存在，不存在则创建并发布
        String l = GeoserverCreateLayerShapefile(manager, ws, store_name, layername, shapefileConfig.getZipFile(), srs, "point");
        System.err.println(w + s + l);
    }

    public static String GeoserverCreateWorkspaces(GeoServerRESTManager manager, String ws) {
        //获取发布对象
        GeoServerRESTPublisher publisher = manager.getPublisher();
        //获取所有的工作空间名称
        List<String> workspaces = manager.getReader().getWorkspaceNames();
        //判断工作空间是否存在
        if (!workspaces.contains(ws)) {
            //创建一个新的存储空间
            boolean createws = publisher.createWorkspace(ws);
            return "创建 ws : " + createws;
        } else {
            return "workspace已经存在了,ws :" + ws;
        }
    }

    public static String GeoserverCreateStoreShapefile(GeoServerRESTManager manager, String ws, String storeName, String url) throws IOException {
        //判断数据存储（datastore）是否已经存在，不存在则创建
        URL urlShapefile = new URL(url);//例：url="file:data/capital.shp"
        RESTDataStore restStore = manager.getReader().getDatastore(ws, storeName);
        if (restStore == null) {
            //创建shape文件存储
            GSShapefileDatastoreEncoder store = new GSShapefileDatastoreEncoder(storeName, urlShapefile);
            store.setCharset(StandardCharsets.UTF_8);
            boolean createStore = manager.getStoreManager().create(ws, store);
            return "创建 store : " + createStore;
        } else {
            return "数据存储已经存在了,store:" + storeName;
        }
    }

    public static String GeoserverCreateLayerShapefile(GeoServerRESTManager manager, String ws, String storeName, String layerName, String path, String srs, String styleName) throws IOException {
        //判断图层是否已经存在，不存在则创建并发布
        RESTLayer layer = manager.getReader().getLayer(ws, layerName);
        //压缩文件的完整路径
        File zipFile = new File(path);
        if (layer == null) {
            //发布图层
            boolean publish = manager.getPublisher().publishShp(ws, storeName, layerName, zipFile, srs, styleName);
            return "publish : " + publish;
            //System.out.println("URL : " + url +"/"+ ws +"/wms");
        } else {
            return "已经发布过了,layer:" + layerName;
            //System.out.println("URL : " + url +"/"+ ws +"/wms");
        }
    }

    public static String GeoserverCreateStorePostGIS(GeoServerRESTManager manager, PostgisConfig postgisConfig, String ws, String storeName) throws IOException {
        //postgis连接配置
        String postgisHost = postgisConfig.getHost();
        int postgisPort = postgisConfig.getPort();//端口号
        String postgisUser = postgisConfig.getUser();//用户名
        String postgisPassword = postgisConfig.getPassword();//用户密码
        String postgisDatabase = postgisConfig.getDatabase();//数据库名称

        //判断数据存储（datastore）是否已经存在，不存在则创建
        RESTDataStore restStore = manager.getReader().getDatastore(ws, storeName);
        if (restStore == null) {
            GSPostGISDatastoreEncoder store = new GSPostGISDatastoreEncoder(storeName);
            store.setHost(postgisHost);//设置url
            store.setPort(postgisPort);//设置端口
            store.setUser(postgisUser);// 数据库的用户名
            store.setPassword(postgisPassword);// 数据库的密码
            store.setDatabase(postgisDatabase);// 那个数据库;
            store.setSchema("public"); //当前先默认使用public这个schema
            store.setConnectionTimeout(20);// 超时设置
            //store.setName(schema);
            store.setMaxConnections(20); // 最大连接数
            store.setMinConnections(1);     // 最小连接数
            store.setExposePrimaryKeys(true);
            boolean createStore = manager.getStoreManager().create(ws, store);
            return "create store : " + createStore;
        } else {
            return "数据存储已经存在了,store:" + storeName;
        }
    }

    public static String GeoserverCreateLayerPostGIS(GeoServerRESTManager manager, String ws, String storeName, String tableName, String srs, String styleName) throws IOException {
        //判断图层是否已经存在，不存在则创建并发布
        RESTLayer layer = manager.getReader().getLayer(ws, tableName);
        if (layer == null) {
            GSFeatureTypeEncoder pds = new GSFeatureTypeEncoder();
            pds.setTitle(tableName);
            pds.setName(tableName);
            pds.setSRS(srs);//"EPSG:4326"
            GSLayerEncoder layerEncoder = new GSLayerEncoder();
            layerEncoder.setDefaultStyle(styleName);
            boolean publish = manager.getPublisher().publishDBLayer(ws, storeName, pds, layerEncoder);
            return "publish : " + publish;
            //System.out.println("URL : " + url +"/"+ ws +"/wms");
        } else {
            return "表已经发布过了,table：" + tableName;
            //System.out.println("URL : " + url +"/"+ ws +"/wms");
        }
    }

    public static String GeoserverCreateStyle(GeoServerRESTManager manager, String ws, String styleName, String styleBody) {
        // style样式
        GeoServerRESTPublisher publisher = manager.getPublisher();
        RESTStyle restStyle = manager.getReader().getStyle(ws, styleName);
        // 判断是否已经发布了style
        if (restStyle == null) {
            boolean createStyle = publisher.publishStyleInWorkspace(ws, styleBody, styleName);
            return "创建样式 " + ws + ":" + styleName + " :" + createStyle;
        } else {
            return "样式已经存在了,style:" + styleName;
        }
    }

    public static void main(String[] args) throws IOException {
        //GeoServer的连接配置
        String url = "http://localhost:8085/geoserver";
        String username = "admin";
        String passwd = "geoserver";
        String path = System.getProperty("user.dir");
        //postgis连接配置
        PostgisConfig pc = new PostgisConfig("localhost", 5432, "postgres", "123456", "webgistest", "testPostgis", "testPostgisDb", "capital", "EPSG:4326");
        GeoserverPublishPostGISData(url, username, passwd, pc);
        //shapefile配置
        ShapefileConfig sc = new ShapefileConfig("testShape", "testShapeStore", "EPSG:4326", path + "/data/capital.zip", "capital", "file:data/capital.shp");
        GeoserverPublishShapefileData(url, username, passwd, sc);
    }

}
