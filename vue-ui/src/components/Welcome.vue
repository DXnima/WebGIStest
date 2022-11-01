<template>
  <div>
    <div align= "center">
      <img align="center" width=300 src="../assets/logo.svg" />
    </div>
    <div align= "center">
      <h2>WebGIStest</h2>
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
    <h4 align="center">
      <a href="https://gitee.com/dxwangnima/WebGIStest">WebGIStest</a> 是基于 Vue+SpringBoot 使用开源GIS技术的相关用例功能实现项目。
    </h4>
    <h2 align="center" id="项目地址">项目地址</h2>
    <div align="center">
      <div>Gitee地址：<a href="https://gitee.com/dxwangnima/WebGIStest">https://gitee.com/dxwangnima/WebGIStest</a></div>
      <div>Github地址：<a href="https://github.com/DXwangnima/WebGIStest">https://github.com/DXwangnima/WebGIStest</a></div>
    </div>
    <h2 align="center">相关技术</h2>
    <div align="center">
      <table align="center">
        <tbody>
        <tr>
          <td><h4 style="margin: 0">前端：</h4></td>
          <td>Vue</td>
          <td>Element</td>
          <td>Axios</td>
          <td>OpenLayers</td>
          <td>Mapbox-GL</td>
          <td>Echarts</td>
        </tr>
        </tbody>
      </table>
      <div align="center">
        <table align="center">
          <tbody>
          <tr>
            <td><h4 style="margin: 0">后端：</h4></td>
            <td>SpringBoot</td>
            <td>Mybaits</td>
            <td>Postgres+PostGIS+PgRouting</td>
            <td>GeoTools</td>
            <td>GDAL</td>
          </tr>
          </tbody>
        </table>
      </div>
      <div align="center">
        <table align="center">
          <tbody>
          <tr>
            <td><h4 style="margin: 0">地图服务端：</h4></td>
            <td>GeoServer</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
    export default {
        name: "Welcome"
    }
</script>

<style>
td{
  padding: 0 5px;
}
</style>
