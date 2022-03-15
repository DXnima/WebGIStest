# WebGIS Test
### java框架: Spring-Boot-mybaits-postgresSQL
### 国内gitee地址：https://gitee.com/dxwangnima/WebGIStest.git
### 技术：
OpenLayers  
postGIS 
GeoTools    
GeoServer REST  

### 准备：
1.下载安装[Geoserver](http://geoserver.org/)

2.下载安装[PostgreSQL](https://www.postgresql.org/) + [PostGIS](https://postgis.net/)

### 说明
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
    - [x] GeoServer发布的WFS服务
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
3. [Geoserver REST](https://docs.geoserver.org/stable/en/user/rest/)
    - [x] 实现PostGIS数据源地图发布
    - [x] 实现Shapefile数据源地图发布
    - [x] 实现SLD样式发布

### 展示
#### 1. 图层切换
![输入图片说明](https://images.gitee.com/uploads/images/2021/0529/163501_8e7de448_4939108.png "屏幕截图.png")
#### 2. 图层多选控件
![输入图片说明](https://images.gitee.com/uploads/images/2021/0529/163533_30fd627e_4939108.png "屏幕截图.png")
#### 3. 鼠标移动获取经纬度
![输入图片说明](https://images.gitee.com/uploads/images/2021/0529/163646_8a4de5ee_4939108.png "屏幕截图.png")
#### 4. 请求数据接口渲染点要素图层
![输入图片说明](https://images.gitee.com/uploads/images/2021/0529/163702_c4ffd01c_4939108.png "屏幕截图.png")
#### 5. 图层联动
![输入图片说明](https://images.gitee.com/uploads/images/2021/0529/163807_39774057_4939108.png "屏幕截图.png")
#### 6. 要素样式修改
![输入图片说明](https://images.gitee.com/uploads/images/2021/0529/163827_a7184669_4939108.png "屏幕截图.png")
#### 7. 地图悬浮窗体
![输入图片说明](https://images.gitee.com/uploads/images/2021/0529/163846_8d1dc79f_4939108.png "屏幕截图.png")
#### 8. 地图覆盖饼状图
![输入图片说明](https://images.gitee.com/uploads/images/2021/0529/163950_86abe0f7_4939108.png "屏幕截图.png")
#### 9. 加载wkts数据 
![输入图片说明](https://images.gitee.com/uploads/images/2021/0529/164002_144d8fe4_4939108.png "屏幕截图.png")
#### 10. 点要素选中高亮
![输入图片说明](https://images.gitee.com/uploads/images/2021/0529/164021_dadcddaa_4939108.png "屏幕截图.png")
#### 11. 点线面要素在线绘制、编辑、保存
![输入图片说明](https://images.gitee.com/uploads/images/2021/0529/164111_88c6ef4f_4939108.png "屏幕截图.png")
#### 12. 综合例子：高校数据显示与查询
![输入图片说明](https://images.gitee.com/uploads/images/2021/0529/164200_0243af12_4939108.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0529/164329_d206d9ff_4939108.png "屏幕截图.png")
#### 13. 网络分析（postgis+pgRouting+geoserver实现）
网络分析参考资料：https://zhuanlan.zhihu.com/p/67232451 里面的”2.1.2、pgRouting“部分。
![输入图片说明](https://images.gitee.com/uploads/images/2021/0902/235524_2bb91255_4939108.png "屏幕截图.png")
#### 14. WFS属性查询和空间查询
![输入图片说明](https://images.gitee.com/uploads/images/2021/1011/142400_56bf36d5_4939108.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/1011/142507_f1b722bb_4939108.png "屏幕截图.png")