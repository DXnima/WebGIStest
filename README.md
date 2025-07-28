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

## CNB云原生启动
1. fork 本仓库, https://cnb.cool/DXnima/WebGIStest
2. 选择main分支，点击WebGIStest启动启动按钮，稍等片刻，即可开始！
3. `使用 WebIDE 打开`，在WebIDE的终端输入 `sh run.sh` ,即可启动项目

<img width="1032" height="590" alt="QQ20250727-142117" src="https://github.com/user-attachments/assets/d4f35979-5b44-4742-adf2-f149a1787453" />


## 项目地址
- Gitee地址：https://gitee.com/dxnima/WebGIStest.git
- Github地址：https://github.com/DXnima/WebGIStest.git

## 技术：

- 前端：Vue | Element | Axios | OpenLayers | Mapbox-GL | Echarts

- 后端：SpringBoot | Mybaits | Redis | Postgres+PostGIS+PgRouting | GeoTools | GDAL v3.5.2

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
        - [x] 最短路径分析（网络分析）
3. [Geoserver REST](https://docs.geoserver.org/stable/en/user/rest/)
    - [x] 实现PostGIS数据源地图发布
    - [x] 实现Shapefile数据源地图发布
    - [x] 实现SLD样式发布
    - [x] 实现PostGIS数据编辑
    - [x] 实现Geoserver REST数据编辑

4. [GDAL](https://gdal.org/download.html)
   - [x] 实现读取.gdb数据
   - [x] 实现将.gdb数据转GeoJSON数据

## 快速启动

### 1. Docker Hub拉取镜像安装
```shell
# 启动容器
docker run -itd \
    -p 28080:28080 \
    -p 28081:28081 \
    -p 28085:28085 \
    --name webgistest \
    -e VUE_APP_BASE_API="http://localhost:28081/webgisapi/" \
    -e VUE_APP_GEOSERVER="http://localhost:28085/" \
    docker.cnb.cool/dxnima/webgistest
```
**容器启动后可以查看项目**：

前端打开：`http://localhost:28080`

后端打开：`http://localhost:28081/webgisapi/doc.html`

geoserver打开：`http://localhost:28085/geoserver`, 用户名：`admin` 密码：`geoserver`

**tips**：
- 如果希望公网访问, 请修改环境变量`VUE_APP_BASE_API`、`VUE_APP_GEOSERVER`中的`localhost`为**服务器公网IP**
- `Gerserver REST` -> `地图服务后台`功能使用
  - 需要执行后端接口配置Geoserver信息
  - 后端地址： `http://localhost:28085/geoserver#/1.0版本/1.配置接口/configGeoserverUsingPOST`
- 容器中还启动了postgres数据库，可以使用`-p 5432:5432`将postgres数据库映射出来

### 2. 自行创建镜像并安装
```shell
git clone https://gitee.com/dxnima/WebGIStest.git
# git clone https://github.com/DXnima/WebGIStest.git
cd WebGIStest
# 构建镜像
docker build -f Dockerfile -t dxnima/webgistest .
# 启动容器
docker run -p 28080:28080 -p 28081:28081 -p 28085:28085 --name webgistest -itd dxnima/webgistest
```
项目打开方式同上

## 编译运行

请移步：
- 前端运行说明[跳转此处](/vue-ui/README.md)
- 后端运行说明[跳转此处](/server-web/README.md)

## 文件说明
```
├─geoserver     geoserver地图服务软件
├─server-web    后端项目
├─SQL           数据库恢复数据表
├─vue-ui        前端项目
```

## 相关技术参考
**1. GIS开发实战图谱**：https://blog.csdn.net/sinat_41310868/article/details/107010972

**2. WebGIS文章汇总**：https://zhuanlan.zhihu.com/p/67232451

**3. PostGIS矢量瓦片创建**
- PostgreSQL+Postgis MapboxVectorTile应用：https://blog.csdn.net/gery1990/article/details/110471047
- 基于springboot+postgis的矢量切片：https://blog.csdn.net/zhaoquanfeng/article/details/81874270
- pgsql+postgis 实时返回mvt(Mapbox Vector Tiles) 矢量瓦片：https://blog.csdn.net/qq_35997793/article/details/108145550

**4. 其他矢量地图解决方案**
- 发布自己的mapbox矢量地图：https://blog.csdn.net/xinlingjun2007/article/details/80846826
- Mapbox-GL矢量地图样式参考：https://blog.csdn.net/wan_yanyan528/article/details/48465403

**5. 网路分析（路径导航）**
- pgRouting官方教程介绍：https://blog.csdn.net/qq_35732147/article/details/88633598
- PostGIS 结合Openlayers以及Geoserver实现最短路径分析：https://blog.csdn.net/guzicheng1990/article/details/102524923
- PostGis+GeoServer+OpenLayers最短路径分析：https://blog.csdn.net/qgbihc/article/details/108635912

**6. GDAL读取gdb**
- java 调用gdal读取gdb数据：https://blog.csdn.net/weixin_53853459/article/details/120954597

## 一起交流
QQ群：515705676

<a target="_blank" href="https://qm.qq.com/cgi-bin/qm/qr?k=BoBCLpD94tPsRCVReo-PBfrAN-fNvWJa&jump_from=webapi&authKey=rT6f5T/iXu6bx2mKuvxvLeaKe/xVcFvCz1ZkrxVSwdEl4uwHZuM3+2Uef4LWJT/Z"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="WebGIStest交流" title="WebGIStest交流"></a>

