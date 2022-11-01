# WebGISTest
后端项目

SpringBoot | Mybaits | Postgres+PostGIS+PgRouting | GeoTools | GDAL

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
server.port=28080
```
### 特别注意端口号

### 生成jar包
```shell
mvn clean
mvn package -Dmaven.test.skip=true 
mvn install -Dmaven.test.skip=true
```


