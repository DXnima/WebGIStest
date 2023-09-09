# WebGISTest
后端项目

SpringBoot | Mybaits | Postgres+PostGIS+PgRouting | GeoTools | GDAL v3.5.2

## 环境安装

**Java环境**：jdk1.8

### 1. 安装Postgres+PostGIS+PgRouting

#### Windows系统安装相关教程参考

1. 安装Postgres + PostGIS: https://zhuanlan.zhihu.com/p/62157728

2. 安装PgRouting: https://zhuanlan.zhihu.com/p/82408769

#### Linux系统安装相关教程参考

CentOS安装参考：https://blog.csdn.net/qq_40953393/article/details/116203749

1. CentOS安装PgRouting:
```shell
# 在CentOS中
sudo yum install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-7-x86_64/pgdg-redhat-repo-latest.noarch.rpm
yum install -y postgresql12-server
sudo /usr/pgsql-12/bin/postgresql-12-setup initdb
yum install -y postgis3_12
yum install -y pgrouting_12   #12代表装的postgresql的版本
```

2. Ubuntu安装PostGres+PostGIS+PgRouting:
```shell
# 在Ubuntu中
sudo echo "deb http://apt.postgresql.org/pub/repos/apt/ bionic-pgdg main" > /etc/apt/sources.list.d/pgdg.list
sudo apt-get update
sudo apt-get install -y postgresql-12-postgis-3
sudo apt-get install -y postgresql-12-pgrouting #12代表装的postgresql的版本
```

### 2. 安装Redis

**Windows安装**：https://www.runoob.com/redis/redis-install.html

**Ubuntu中安装**
```shell
sudo apt-get -y redis-server
```


### 3. 安装GDAL(**版本要求3.5.2**)

配置参考：

**Windows安装**：https://www.jianshu.com/p/c9c385395ada

**Linux安装**：https://www.jianshu.com/p/ff4cf2b59613

**Ubuntu中安装**
```shell
sudo apt-get -y libgdal-dev
```

**Linux中编译安装**
```shell
# 安装proj
wget http://download.osgeo.org/proj/proj-8.2.0.tar.gz
tar -zxvf proj-8.2.0.tar.gz
cd proj-8.2.0
./configure
make && make install

# 安装geos
wget https://download.osgeo.org/geos/geos-3.11.0.tar.bz2
tar -xjf geos-3.11.0.tar.bz2
cd geos-3.11.0
./configure
make && make install

# 安装swig的依赖pcre2
apt-get install libpcre2-dev
# 安装swig
wget http://prdownloads.sourceforge.net/swig/swig-4.1.0.tar.gz
tar -zxvf swig-4.1.0.tar.gz
cd swig-4.1.0
./configure
make && make install
swig -version

# 安装ant
wget https://archive.apache.org/dist/ant/binaries/apache-ant-1.10.12-bin.tar.gz
tar -zxvf apache-ant-1.10.12-bin.tar.gz
cd apache-ant-1.10.12
# ant需要配置环境变量
vi /etc/profile
export ANT_HOME=/usr/local/apache-ant-1.10.12
export PATH=$ANT_HOME/bin:$PATH
source /etc/profile
ant -version

# 安装gdal相关依赖
sudo apt-get install libgdal-dev
# 安装gdal
wget http://download.osgeo.org/gdal/3.5.2/gdal-3.5.2.tar.gz
tar -xf gdal-3.5.2.tar.gz
cd gdal-3.5.2
./configure
make && make install
gdalinfo --version
```

#### 特别注意：

1. Windows安装中环境变量不能漏

|环境变量名|环境变量值|
|    :----   |    :----   |
|path	|C:\Program Files\GDAL\|
|GDAL_DATA	|C:\Program Files\GDAL\gdal-data|
|GDAL_DRIVER_PATH	|C:\Program Files\GDAL\gdalplugins|
| PROJ_LIB        | C:\Program Files\GDAL\projlib   |

2. dll复制到bin目录

### 4. Postgres数据库恢复

`PostGres+PostGIS+PgRouting`都安装好了方可进行数据库恢复

[webgistest.sql](/SQL/webgistest.sql) 是**所有数据**库导入文件

**直接恢复**：

直接执行[webgistest.sql](/SQL/webgistest.sql)文件即可实现数据库恢复

恢复命令：
```shell
psql -c 'CREATE DATABASE webgistest;'
# webgistest.sql文件路径要注意修改
psql -U postgres -d webgistest -q -f /webgistest/SQL/webgistest.sql
```

**按需导入说明如下**：

1. 创建数据库(数据库名称：webgistest)
```shell
CREATE DATABASE webgistest;
```
2. 添加空间扩展(必须执行)
```shell
CREATE EXTENSION postgis;
CREATE EXTENSION pgrouting;
```

各表SQL文件在[sql](/SQL/sql/)目录下，根据需要按需导入，各文件说明如下：

|   文件名   | 说明         | 备注             |
|    :----   |:-----------|:---------------|
|[capital.sql](/SQL/sql/capital.sql)| 数据表        | 必须导入           |
|[layer_edit.sql](/SQL/sql/layer_edit.sql)| 图层编辑表      | 必须导入           |
|[layer_university.sql](/SQL/sql/layer_university.sql)| 高校数据表      | 必须导入           |
|[port.sql](/SQL/sql/port.sql)| 数据表        | 必须导入           |
|[province.sql](/SQL/sql/province.sql)| 省级行政区表     | 必须导入           |
|[mvt_test.sql](/SQL/sql/mvt_test.sql)| 存储矢量瓦片表    | 非必须，包含部分矢量瓦片缓存 |
|[test_polygon.sql](/SQL/sql/test_polygon.sql)| 存储矢量瓦片表    | 非必须，包含部分矢量瓦片缓存 |
|[mvt_function.sql](/SQL/sql/mvt_function.sql)| 生成矢量瓦片重要函数 | 最后执行           |
|[shenzhen_roads.sql](/SQL/sql/shenzhen_roads.sql)| 路网导航数据表    | 必须导入           |
|[shenzhen_creat_network.sql](/SQL/sql/shenzhen_creat_network.sql)| 生成路网导航相关函数 | 最后执行           |

### 5. 启动Geoserver

**注意**：启动前需要安装[jdk 1.8](https://www.oracle.com/cn/java/technologies/downloads/#java8)

- 双击[geoserver/bin/startup.bat](/geoserver/bin/startup.bat)

&emsp; 或

- 双击[/geoserver/bin/startup.sh](/geoserver/bin/startup.sh)

**注意**： 
```
默认启动端口：28085
用户名：admin
密码：geoserver
启动地址：http://localhost:28085/geoserver
```

## 如何启动

1. 修改数据库用户名密码

**修改文件位置**：[application.properties](/server-web/src/main/resources/application.properties)
```properties
# postgres数据库webgistest
spring.datasource.url=jdbc:postgresql://localhost:5432/webgistest
# postgres数据库用户名
spring.datasource.username=postgres
# postgres数据库密码
spring.datasource.password=123456
# 端口号
server.port=28081
```
### 特别注意端口号

### 生成jar包
```shell
mvn clean
mvn package -Dmaven.test.skip=true 
```
### 启动jar包
```
java -jar -Dfile.encoding=utf-8 WebGIStest-0.1.jar
```

使用`--port`可以设置端口(默认是28081)
```
java -jar -Dfile.encoding=utf-8 WebGIStest-0.1.jar --port=8081
```

使用`--file`可以设置文件存储位置(默认是: F:/A, Linux系统必须修改)
```
java -jar -Dfile.encoding=utf-8 WebGIStest-0.1.jar --file=/webgistest/files
```

