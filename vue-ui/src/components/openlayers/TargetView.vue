<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>OpenLayers</el-breadcrumb-item>
      <el-breadcrumb-item>Target和View</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <p class="tool-box">
        <button id="setTarget" @click="setTargetClick">setTarget</button>
        <button id="setView" @click="setViewClick">setView</button>
      </p>
      <el-container>
        <el-aside width="50%">
          <div id="map1" class="map" style="width:100%;height:100%"></div>
        </el-aside>
        <el-main>
          <div id="map2" class="map" style="width:100%;height:100%"></div>
        </el-main>
      </el-container>


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
    return {
      view1: null,
      view2: null,
      target: null,
      map: null
    }
  },
  created() {
  },
  mounted() {
    this.initMap()
  },
  methods: {
    initMap() {
      this.view1 = new View({
        zoom: 4,
        center: proj.transform([110, 39], "EPSG:4326", "EPSG:3857")
      });
      this.view2 = new View({
        zoom: 8,
        center: proj.fromLonLat([110, 39])
      });

      this.target = "map1"
      this.view = this.view1;
      this.map = new Map({
        controls: control.defaults({
          attribution: false
        }).extend([]),
        target: this.target,
        layers: [
          getTdtLayer("vec_w"),
          getTdtLayer("cva_w")
        ],
        view: this.view
      });
    },
    setTargetClick() {
      if (this.target === "map1") {
        this.target = "map2";
        this.map.set("target", this.target);
      } else {
        this.target = "map1";
        this.map.setTarget(this.target);
      }
    },
    setViewClick() {
      if (this.view === this.view1) {
        this.view = this.view2;
      } else {
        this.view = this.view1;
      }
      this.map.setView(this.view);
    }
  }
}
</script>

<style lang="less" scoped>
.tool-box {
  padding: 5px 10px;
}

.map {
  border: 1px solid red;
}
</style>
