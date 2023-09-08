<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>OpenLayers</el-breadcrumb-item>
      <el-breadcrumb-item>获取经纬度</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <div id="map" style="width:100%;height:100%">
        <div id="mousepos"></div>
      </div>
    </el-card>
  </div>
</template>
<script>
import Map from 'ol/map';
import View from 'ol/view';
import control from 'ol/control';
import proj from 'ol/proj';
import {getTdtLayer} from "../../utils/searchUtils";
export default {
  data() {
    return {}
  },
  created() {
  },
  mounted() {
    this.initMap()
  },
  methods: {
    initMap() {
      let map = new Map({
        controls: control.defaults({
          attribution: false
        }).extend([]),
        target: 'map',
        layers: [
          getTdtLayer("vec_w"),
          getTdtLayer("cva_w")
        ],
        view: new View({
          zoom: 4,
          center: proj.transform([110, 39], "EPSG:4326", "EPSG:3857")
        })
      });

      map.on("pointermove", function (evt) {
        let lonlat = proj.transform(evt.coordinate, "EPSG:3857", "EPSG:4326");
        document.getElementById("mousepos").innerHTML = lonlat[0].toFixed(3) + ", " + lonlat[1].toFixed(3);
      });
    }
  }
}
</script>

<style lang="less" scoped>

</style>
