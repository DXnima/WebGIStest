#!/bin/bash

# 启动 PostgreSQL 服务
service postgresql start
echo "PostgreSQL started"
# 检查 PostgreSQL 服务是否启动成功
if [ $? -eq 0 ]; then
  echo "PostgreSQL started successfully"
else
  echo "PostgreSQL failed to start"
fi

# 启动 Redis 服务
service redis-server start
echo "Redis started"
# 检查 PostgreSQL 服务是否启动成功
if [ $? -eq 0 ]; then
  echo "PostgreSQL started successfully"
else
  echo "PostgreSQL failed to start"
fi

# 启动 HTTP 服务器并提供 Vue.js 应用
http-server vue-ui/dist -p 28080 &
HTTP_SERVER_PID=$!
# 等待 HTTP 服务器启动
sleep 5  # 可能需要根据实际情况调整等待时间
# 检查 HTTP 服务器是否启动成功
if ps -p $HTTP_SERVER_PID > /dev/null; then
  echo "HTTP server for Vue.js started successfully"
else
  echo "HTTP server for Vue.js failed to start"
fi

# 启动geoserver
sh /webgistest/geoserver/bin/startup.sh &
GEOSERVER_PID=$!
# 等待 geoserver 启动
sleep 5  # 可能需要根据实际情况调整等待时间
# 检查 geoserver 是否启动成功
if ps -p $GEOSERVER_PID > /dev/null; then
  echo "Geoserver started successfully"
else
  echo "Geoserver failed to start"
fi

# 启动jar
cd /webgistest/server-web/
java -jar WebGIStest-0.1.jar
JAVA_APP_PID=$!
# 等待 Java 应用启动
sleep 5  # 可能需要根据实际情况调整等待时间
# 检查 Java 应用是否启动成功
if ps -p $JAVA_APP_PID > /dev/null; then
  echo "Java application started successfully"
else
  echo "Java application failed to start"
fi