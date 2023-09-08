# Start from osgeo/gdal:ubuntu-small-3.5.2
FROM osgeo/gdal:ubuntu-small-3.5.2

# 换源（可以根据需要选择）
RUN sed -i "s@http://.*archive.ubuntu.com@http://repo.huaweicloud.com@g" /etc/apt/sources.list
RUN sed -i "s@http://.*security.ubuntu.com@http://repo.huaweicloud.com@g" /etc/apt/sources.list

# 设置时区
RUN ln -fs /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 更新Repos和安装依赖项
RUN apt-get update \
    && apt-get -y install gnupg curl wget \
    redis-server openjdk-8-jdk maven

# 安装 Node.js（16.x）
RUN curl -sL https://deb.nodesource.com/setup_16.x | bash - && \
    apt-get -y install nodejs

# 添加 PostgreSQL 存储库的 GPG 密钥
RUN wget --quiet -O - https://mirrors.tuna.tsinghua.edu.cn/postgresql/repos/apt/ACCC4CF8.asc | apt-key add -

# 添加 PostgreSQL 存储库，并指定版本
RUN echo "deb https://mirrors.tuna.tsinghua.edu.cn/postgresql/repos/apt/ focal-pgdg main" > /etc/apt/sources.list.d/pgdg.list
# 安装软件
RUN apt-get update \
    && apt-get -y install postgresql-12 postgresql-12-postgis-3 postgresql-12-pgrouting

# 设置环境变量
ENV WEBGIS /webgistest
ENV GEOSERVER_HOME /webgistest/geoserver
# 设置 PostgreSQL 密码
ENV POSTGRES_PASSWORD 123456

# 项目文件复制
RUN mkdir -p ${WEBGIS}
WORKDIR ${WEBGIS}/
COPY . ${WEBGIS}/

# 创建数据库，并恢复数据
RUN service postgresql start \
    && su postgres -c "psql -c 'ALTER USER postgres PASSWORD '\''123456'\'';'" \
    && su postgres -c "psql -c 'CREATE DATABASE webgistest;'" \
    && su postgres -c "psql -U postgres -d webgistest -a -f /webgistest/SQL/webgistest.sql"

# 切换到 vue-ui 子目录安装依赖并构建前端项目
WORKDIR ${WEBGIS}/vue-ui
RUN npm i -g http-server --registry https://registry.npmmirror.com && \
    npm i --registry https://registry.npmmirror.com && npm run build

# 设置 Maven 镜像源（可以根据需要选择）
RUN rm -rf /usr/share/maven/conf/settings.xml
COPY settings.xml /usr/share/maven/conf/

# 切换到 server-web 子目录安装依赖并构建后端项目
WORKDIR ${WEBGIS}/server-web
RUN mvn clean && mvn package -Dmaven.test.skip=true

# 切换到住目录
WORKDIR ${WEBGIS}/
RUN mv server-web/target/WebGIStest-0.1.jar server-web/
# 清理安装过程中的临时文件
RUN rm -rf SQL server-web/target server-web/src \
    vue-ui/image vue-ui/src vue-ui/node vue-ui/node_modules

# 启动相关服务
CMD ["sh", "docker_start.sh"]