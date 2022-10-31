<div align= "center">
<img align="center" width=200 src="vue-ui/src/assets/logo.svg" />
</div>


<div align= "center">
 <h1>WebGIStest</h1>

 <a href="https://github.com/openlayers/openlayers">
    <img alt="Openlayers" width=40px height=40px src="https://openlayers.org/en/v4.6.5/apidoc/logo-70x70.png" />OpenLayers
</a>
 <a href="https://github.com/geoserver/geoserver">
    <img alt="Geoserver" width=165px height=40px src="https://geoserver.org/img/geoserver-logo.png" />
 </a>
<a href="https://github.com/geotools/geotools">
    <img alt="Geotools" width=165px height=40px src="https://geotools.org/_static/img/geotools-logo.png">
</a>

<a href="https://www.osgeo.org/">
    <img alt="OSGeo" width=120px height=40px src="https://geoserver.org/img/osgeo-logo.png">
</a>

</div>

<h4 align="center">WebGIStest 是基于 Vue+SpringBoot </a> 使用开源GIS技术的相关用例功能实现项目。</h4>

## 项目地址
- Gitee地址：https://gitee.com/dxwangnima/WebGIStest.git
- Github地址：https://github.com/DXwangnima/WebGIStest.git

## 技术：

- 前端：Vue | Element | Axios | OpenLayers | Mapbox-GL | Echarts

- 后端：SpringBoot | Mybaits | Postgres+PostGIS+PgRouting | GeoTools | GDAL

- 地图服务端： GeoServer

## 项目展示
![空间分析模块](vue-ui/image/3.png "空间分析")

## 项目说明

- 前端项目[跳转此处](/vue-ui/README.md)
- 后端项目[跳转此处](/server-web/README.md)

## 实现功能说明
1. [OpenLayers](https://openlayers.org/en/latest/apidoc/)
    - [x] 实现图层切换
      - [x] openStreetMap
      - [x] 百度地图
      - [x] 天地图
      - [x] WMS Image服务
      - [x] WMS Tile服务
      - [x] WMS Grid服务
      - [x] WMTS服务
      - [x] WFS服务
    - [x] 实现图层多选控件
    - [x] 鼠标移动获取经纬度
    - [x] 请求数据接口渲染点要素图层
    - [x] 要素样式修改 
    - [x] 地图悬浮窗体
    - [x] 地图覆盖饼状图
    - [x] 加载GeoJSON数据
    - [x] 加载wkts数据  
    - [x] 加载GeoServer发布的wms服务
    - [x] 加载GeoServer发布的WFS服务
      - [x] 加载WFS服务
      - [x] 添加WFS服务图层
      - [x] 修改WFS服务图层
      - [x] 删除WFS服务
    - [x] GeoServer实现最短路径分析（网络分析）
    - [x] 点要素选中高亮
    - [x] 点线面要素在线绘制、编辑
    - [x] 点线面要素保存PostgesSQL数据库
    - [x] PostGIS实现矢量切片
    - [x] 综合例子：高校数据显示与查询
2. [GeoTools](http://docs.geotools.org/latest/userguide/tutorial/quickstart/maven.html)
    - [x] 启动Quickstart例子
    - [x] Geojson转Shapefile
    - [x] Shapefile转Geojson
    - [x] 读取Shapefile
    - [x] Shapefile进行Intersect计算
    - [x] Shapefile坐标转换
    - [x] 创建XML格式的SLD
    - [x] 空间关系判断
    - [x] 空间分析
        - [x] 叠加分析
        - [x] 合并分析
        - [x] 差异分析
        - [x] 缓冲区分析
3. [Geoserver REST](https://docs.geoserver.org/stable/en/user/rest/)
    - [x] 实现PostGIS数据源地图发布
    - [x] 实现Shapefile数据源地图发布
    - [x] 实现SLD样式发布

4. [GDAL](https://gdal.org/download.html)
   - [x] 实现读取.gdb数据
   - [x] 实现将.gdb数据转GeoJSON数据

## 提前安装

### 1. 安装Postgres+PostGIS+PgRouting

#### Windows系统安装相关教程参考

1. 安装Postgres + PostGIS: `https://zhuanlan.zhihu.com/p/62157728`

2. 安装PgRouting: `https://zhuanlan.zhihu.com/p/82408769`

#### Linux系统安装相关教程参考

1. CentOS安装参考：`https://blog.csdn.net/qq_40953393/article/details/116203749`

2. CentOS安装PgRouting:
```shell
# 在CentOS中
yum install pgrouting_12   #12代表装的postgresql的版本
```

3. Ubuntu安装PostGres+PostGIS+PgRouting:
```shell
# 在Ubuntu中
sudo apt-get install postgresql-12-postgis-3
sudo apt install postgresql-12-pgrouting #12代表装的postgresql的版本
```

### 2. 安装GDAL

配置参考：`https://www.jianshu.com/p/c9c385395ada` 

#### 注意两点：

1. 环境变量不能漏

|环境变量名|环境变量值|
|    :----   |    :----   |
|path	|C:\Program Files\GDAL\|
|GDAL_DATA	|C:\Program Files\GDAL\gdal-data|
|GDAL_DRIVER_PATH	|C:\Program Files\GDAL\gdalplugins|
| PROJ_LIB        | C:\Program Files\GDAL\projlib   |

2. dll复制到bin目录

### 3. Postgres数据库恢复

`PostGres+PostGIS+PgRouting`都安装好了方可进行数据库恢复

1. 创建数据库(数据库名称：webgistest)
```shell
CREATE DATABASE webgistest;
```
2. 添加空间扩展(必须执行)
```shell
CREATE EXTENSION postgis;
CREATE EXTENSION pgrouting;
```
3. [SQL](/SQL)下的sql文件导入数据库

|   文件名   | 说明         | 备注             |
|    :----   |:-----------|:---------------|
|[capital.sql](/SQL/capital.sql)| 数据表        | 必须导入           |
|[layer_edit.sql](/SQL/layer_edit.sql)| 图层编辑表      | 必须导入           |
|[layer_university.sql](/SQL/layer_university.sql)| 高校数据表      | 必须导入           |
|[port.sql](/SQL/port.sql)| 数据表        | 必须导入           |
|[province.sql](/SQL/province.sql)| 省级行政区表     | 必须导入           |
|[mvt_test.sql](/SQL/mvt_test.sql)| 存储矢量瓦片表    | 非必须，包含部分矢量瓦片缓存 |
|[mvt_function.sql](/SQL/mvt_function.sql)| 生成矢量瓦片重要函数 | 最后执行           |
|[shenzhen_roads.sql](/SQL/shenzhen_roads.sql)| 路网导航数据表    | 必须导入           |
|[shenzhen_creat_network.sql](/SQL/shenzhen_creat_network.sql)| 生成路网导航相关函数 | 最后执行           |

### 4. 启动Geoserver

- 双击[geoserver/bin/startup.bat](/geoserver/bin/startup.bat)

&emsp; 或

- 双击[/geoserver/bin/startup.sh](/geoserver/bin/startup.sh)

注意： 
```
默认启动端口：28081
用户名：admin
密码：geoserver
启动地址：http://localhost:28081/geoserver
```

## 文件说明
```
├─geoserver     geoserver地图服务软件
├─server-web    后端项目
├─SQL           数据库恢复数据表
├─vue-ui        前端项目
```

## 相关技术参考
1. GIS开发实战图谱：https://blog.csdn.net/sinat_41310868/article/details/107010972
2. WebGIS文章汇总：https://zhuanlan.zhihu.com/p/67232451
3. PostGIS矢量瓦片创建
- PostgreSQL+Postgis MapboxVectorTile应用：https://blog.csdn.net/gery1990/article/details/110471047
- 基于springboot+postgis的矢量切片：https://blog.csdn.net/zhaoquanfeng/article/details/81874270
- pgsql+postgis 实时返回mvt(Mapbox Vector Tiles) 矢量瓦片：https://blog.csdn.net/qq_35997793/article/details/108145550

4. 其他矢量地图解决方案
- 发布自己的mapbox矢量地图：https://blog.csdn.net/xinlingjun2007/article/details/80846826
- Mapbox-GL矢量地图样式参考：https://blog.csdn.net/wan_yanyan528/article/details/48465403

5. 网路分析（路径导航）
- pgRouting官方教程介绍：https://blog.csdn.net/qq_35732147/article/details/88633598
- PostGIS 结合Openlayers以及Geoserver实现最短路径分析：https://blog.csdn.net/guzicheng1990/article/details/102524923
- PostGis+GeoServer+OpenLayers最短路径分析：https://blog.csdn.net/qgbihc/article/details/108635912

6. GDAL读取gdb
- java 调用gdal读取gdb数据：https://blog.csdn.net/weixin_53853459/article/details/120954597
