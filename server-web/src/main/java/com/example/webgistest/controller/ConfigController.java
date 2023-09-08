package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.constant.RedisKey;
import com.example.webgistest.geoserver.ImproveGeoServerPublisher;
import com.example.webgistest.geoserver.ImprovePostGISDatastore;
import com.example.webgistest.pojo.PgTableInfo;
import com.example.webgistest.service.IPostGISService;
import com.example.webgistest.utils.RedisUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import it.geosolutions.geoserver.rest.HTTPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置配置
 */
@ApiSort(value = 1)
@Api(tags = "1.配置接口")
@Controller
@RequestMapping("config")
public class ConfigController {

    @Resource
    private RedisUtil redisUtil;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "1.Geoserver配置", notes = "配置Geoserver服务的url,用户名，密码。")
    @RequestMapping(value = "/geoserver", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Object> configGeoserver(@RequestBody ImproveGeoServerPublisher config) {

        if (!HTTPUtils.httpPing(config.getRestURL(), config.getUsername(), config.getPassword())) {
            return ServerResponse.createByErrorMessage("Geoserver服务器无响应，请重新配置！");
        }

        String key = RedisKey.GEOSERVER_CONFIG;
        //查询缓存中是否存在
        boolean hasKey = redisUtil.hasKey(key);
        Map<String, Object> map = new HashMap<>();
        map.put("restURL", config.getRestURL());
        map.put("username", config.getUsername());
        map.put("password", config.getPassword());
        redisUtil.hmset(key, map);
        if (!hasKey) {
            return ServerResponse.createBySuccess("Geoserver配置成功！", redisUtil.hmget(key));
        } else {
            return ServerResponse.createBySuccess("Geoserver配置修改成功！", redisUtil.hmget(key));
        }
    }

    @Autowired
    IPostGISService iPostGISService;

    /**
     * 连接PostGIS数据库并获取表名
     *
     * @param postgis postgis连接配置
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "2.PostGIS获取表和类型", notes = "连接PostGIS数据库并获取表名,不需要dataStoreName参数.")
    @RequestMapping(value = "/postgis", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<List<PgTableInfo>> getTableInfo(@RequestBody ImprovePostGISDatastore postgis) {
        return iPostGISService.getTableInfo(postgis);
    }

}
