<template>
    <div>
        <!--面包屑导航区域-->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>GDAL</el-breadcrumb-item>
            <el-breadcrumb-item>读取gdb</el-breadcrumb-item>
        </el-breadcrumb>
        <!--卡片区域-->
        <el-card>
            <div id="map"></div>
            <el-table class="tb" :data="tableData" style="width: 800px">
                <el-table-column prop="name" label="行政区划名称">
                </el-table-column>
                <el-table-column prop="ha" label="治理河岸长度（km）">
                </el-table-column>
                <el-table-column prop="zlgc" label="治理工程量（km）">
                </el-table-column>
            </el-table>
        </el-card>
    </div>
</template>
<script>
import Map from 'ol/map';
import View from 'ol/view';
import VectorLayer from 'ol/layer/vector';
import Vector from 'ol/source/vector';
import GeoJSON from 'ol/format/geojson';
import Style from 'ol/style/style';
import Stroke from 'ol/style/stroke';
import Fill from 'ol/style/fill';
import {getTdtLayer} from "../../utils/searchUtils";
export default {
  data() {
    return {
      tableData: [],
      map: null
    }
  },
  created() {
  },
  mounted() {
    this.initMap()
  },
  methods: {
    async initMap() {
      await this.getTableData()
      const layers = await this.getLayers()
      console.log(layers.length)
      let map = new Map({
        layers: [
          getTdtLayer("vec_w"),
          getTdtLayer("cva_w")
        ],
        // 设置显示地图的视图
        view: new View({
          projection: 'EPSG:3857',
          center: [1.2894645034200002E7, 3470216.539300002], // 定义地图显示中心
          zoom: 11,
        }),
        target: "map", //存放地图的容器
      });
      this.map = map
      layers.forEach(layer => {
        this.addFeature(layer)
      })
    },
    async getTableData() {
      const {data: res} = await this.$API.getCalculate()
      if (res.status === 200)
        this.tableData = res.data
    },
    async getLayers() {
      const {data:res} = await this.$API.getGdbLayers()
      if (res.status === 200)
        return res.data.reverse()
    },
    addFeature(layer) {
      let geoJson = {
        "type": "FeatureCollection",
        "features": layer.features.map(item => JSON.parse(item.geometry))
      }
      let features = (new GeoJSON()).readFeatures(geoJson);
      let style
      switch (layer.name) {
        case "治理工程":
          style = new Style({
            // 设置线颜色\宽度
            stroke: new Stroke({
              width: 1,
              color: "#ff0000"
            })
          })
          break;
        case "行政区划":
          style = new Style({
            // 设置线颜色\宽度
            stroke: new Stroke({
              width: 1,
              color: "#565454"
            }),
            // 图形区域内颜色
            fill: new Fill({
              color: "#e1e1e1"
            })
          })
          break;
        case "河岸":
          style = new Style({
            // 设置线颜色\宽度
            stroke: new Stroke({
              width: 1,
              color: "#0070ff"
            })
          });
          break;

      }
      let vectorSource = new Vector({
        features: features
      });
      let vector = new VectorLayer({
        source: vectorSource,
        style: style,
        renderMode: "vector"
      });
      this.map.addLayer(vector);
    }
  }
}
</script>

<style>
.tb {
    position: absolute;
    top: 130px;
    right: 50px;
    z-index: 1001;
    padding: 5px 8px;
}
</style>
