/**
 *  API 统一管理
 * */
// 获取capital表数据
export * from './control/capital'
// Geoserver、Pg配置接口
export * from './control/config'
// GDAL读取gdb数据
export * from './control/gdb'
// Geotools发布WMS服务
export * from './control/geotools'
// 编辑空间数据测试
export * from './control/layeredit'
// 地图服务相关信息接口
export * from './control/mapinfo'
// PostGIS生成矢量瓦片
export * from './control/mapmvt'
// 地图服务操作
export * from './control/mappublish'
// 地图样式接口
export * from './control/mapstyle'
// Pg数据库数据编辑接口
export * from './control/pgedit'
// 空间分析接口
export * from './control/spatialanalysis'
// 获取大学数据
export * from './control/university'
// 文件上传
export * from './control/upload'
// Geoserver REST WFS封装
export * from './ogc/wfs'
// Geoserver REST WMS封装
export * from './ogc/wms'
