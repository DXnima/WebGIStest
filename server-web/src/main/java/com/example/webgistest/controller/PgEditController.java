package com.example.webgistest.controller;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.PgTableInfo;
import com.example.webgistest.pojo.PgRowInfo;
import com.example.webgistest.pojo.PgFieldTableInfo;
import com.example.webgistest.service.IPgEditService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 设置PostgresSQL配置
 */
@ApiSort(value = 7)
@Api(tags = "7.Pg数据库数据编辑接口")
@Controller
@RequestMapping("pgedit")
public class PgEditController {

    @Autowired
    IPgEditService iPgEditService;

    /**
     * 连接PostGIS本地数据库并获取表名
     *
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "1.获取表名", notes = "连接PostGIS本地数据库并获取表名.")
    @RequestMapping(value = "/table", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<PgTableInfo>> getTableInfo() {
        return iPgEditService.getTableInfo();
    }

    /**
     * 连接PostGIS数据库并获取数据
     *
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "2.获取指定表数据", notes = "获取指定表数据.")
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Map<String, Object>> getTableData(@RequestBody PgFieldTableInfo pgFieldTableInfo) {
        return iPgEditService.getTableData(pgFieldTableInfo);
    }

    /**
     * 连接PostGIS数据库并查询表数据
     *
     * @return 是否创建成功
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "3.查询指定表数据", notes = "查询指定表数据.")
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Map<String, Object>> findTableData(@RequestBody PgFieldTableInfo pgFieldTableInfo){
        return iPgEditService.findTableData(pgFieldTableInfo);
    }

    /**
     * 添加数据
     *
     * @return 是否成功
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "4.添加数据", notes = "添加数据.")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addTableData(@RequestBody PgRowInfo pgRowInfo){
        return iPgEditService.addTableData(pgRowInfo);
    }

    /**
     * 更新数据
     *
     * @return 是否成功
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "5.更新数据", notes = "更新数据.")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> updateTableData(@RequestBody PgRowInfo pgRowInfo){
        return iPgEditService.updateTableData(pgRowInfo);
    }

    /**
     * 删除数据
     *
     * @return 是否成功
     */
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "6.删除数据", notes = "删除数据.")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> deleteTableData(@RequestBody PgRowInfo pgRowInfo){
        return iPgEditService.deleteTableData(pgRowInfo);
    }

}
