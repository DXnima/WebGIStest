#!/bin/bash

# 文件路径
FILE_PATH="/webgistest/vue-ui/dist/maputnik/mvt_test.json"

# 检查文件是否存在
if [ ! -f "$FILE_PATH" ]; then
  echo "错误: 文件不存在: $FILE_PATH"
  exit 1
fi

# 使用sed替换URL
# 使用不同的分隔符(#)来避免URL中的斜杠造成问题
sed -i "s#http://localhost:28081/webgisapi/#${VUE_APP_BASE_API}#g" "$FILE_PATH"

# 替换BASEURL
JS_DIR="/webgistest/vue-ui/dist/js"
echo "开始替换文件中的 URL..."
for file in $(find "$JS_DIR" -type f); do
    # 检查文件是否包含目标字符串
    if grep -q "http://localhost:28081/webgisapi/" "$file"; then
        # 使用 sed 替换字符串
        sed -i "s#http://localhost:28081/webgisapi/#${VUE_APP_BASE_API}#g" "$file"
    fi
    if grep -q "http://localhost:28085/" "$file"; then
        # 使用 sed 替换字符串
        sed -i "s#http://localhost:28085/#${VUE_APP_GEOSERVER}#g" "$file"
    fi
done

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
# 检查 Redis 服务是否启动成功
if [ $? -eq 0 ]; then
  echo "Redis started successfully"
else
  echo "Redis failed to start"
fi

# # 启动 Nginx 服务
service nginx start
echo "Nginx started"
# 检查 Nginx 服务是否启动成功
if [ $? -eq 0 ]; then
  echo "Nginx started successfully"
else
  echo "Nginx failed to start"
fi

# 启动 HTTP 服务器并提供 Vue.js 应用
# http-server vue-ui/dist -p 28080 &
# HTTP_SERVER_PID=$!
# # 等待 HTTP 服务器启动
# sleep 5  # 可能需要根据实际情况调整等待时间
# # 检查 HTTP 服务器是否启动成功
# if ps -p $HTTP_SERVER_PID > /dev/null; then
#   echo "HTTP server for Vue.js started successfully"
# else
#   echo "HTTP server for Vue.js failed to start"
# fi

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
java -jar WebGIStest-0.1.jar --file=/webgistest/files
JAVA_APP_PID=$!
# 等待 Java 应用启动
sleep 5  # 可能需要根据实际情况调整等待时间
# 检查 Java 应用是否启动成功
if ps -p $JAVA_APP_PID > /dev/null; then
  echo "Java application started successfully"
else
  echo "Java application failed to start"
fi