<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>OpenLayers</el-breadcrumb-item>
      <el-breadcrumb-item>图层联动</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <div id="map1" class="map"></div>
      <div id="map2" class="map"></div>
      <div id="map3" class="map"></div>
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
      let view = new View({
        zoom: 4,
        center: proj.transform([110, 39], "EPSG:4326", "EPSG:3857")
      });

      let map1 = new Map({
        controls: control.defaults({
          attribution: false
        }).extend([]),
        target: "map1",
        layers: [
          getTdtLayer("vec_w"),
          getTdtLayer("cva_w")
        ],
        view: view
      });

      let map2 = new Map({
        controls: control.defaults({
          attribution: false
        }).extend([]),
        target: "map2",
        layers: [
          getTdtLayer("img_w")
        ],
        view: view
      });

      let map3 = new Map({
        controls: control.defaults({
          attribution: false
        }).extend([]),
        target: "map3",
        layers: [
          getTdtLayer("ter_w")
        ],
        view: view
      });
    }
  }
}
</script>

<style lang="less" scoped>
.map {
  height: 400px;
  float: left;
}
</style>
