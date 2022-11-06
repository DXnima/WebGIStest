<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>空间分析</el-breadcrumb-item>
      <el-breadcrumb-item>缓冲区分析</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <el-row>
        <el-col :span="10">
          <el-input type="textarea" :rows="4" placeholder="请输入WKT" v-model="geom1"></el-input>
        </el-col>
        <el-col :span="10">
          <el-input type="textarea" :rows="4" placeholder="显示结果" v-model="geom"></el-input>
        </el-col>
        <el-col :span="4">
          <el-input-number v-model="distance" :min="0" label="描述文字"></el-input-number>
          <el-button type="primary" @click="analysis">提交</el-button>
        </el-col>
      </el-row>
      <div id="map"></div>
    </el-card>
  </div>
</template>
<script>
import {getTdtLayer, styleFunction} from "../../utils/searchUtils";
import Map from 'ol/map';
import View from 'ol/view';
import Feature from 'ol/feature';
import Point from 'ol/geom/point';
import TileLayer from 'ol/layer/tile';
import ImageLayer from 'ol/layer/image';
import VectorLayer from 'ol/layer/vector';
import GroupLayer from 'ol/layer/group';
import TileGrid from 'ol/tilegrid/tilegrid';
import WMTSGrid from 'ol/tilegrid/wmts';
import OSM from "ol/source/osm";
import XYZ from 'ol/source/xyz';
import TileImage from 'ol/source/tileimage';
import ImageWMS from 'ol/source/imagewms';
import TileWMS from 'ol/source/tilewms';
import WMTS from 'ol/source/wmts';
import Vector from 'ol/source/vector';
import GeoJSON from 'ol/format/geojson';
import WKT from 'ol/format/wkt';
import WFS from 'ol/format/wfs';
import Draw from 'ol/interaction/draw';
import Select from 'ol/interaction/select';
import Modify from 'ol/interaction/modify';
import Style from 'ol/style/style';
import Icon from 'ol/style/icon';
import Stroke from 'ol/style/stroke';
import Text from 'ol/style/text';
import Fill from 'ol/style/fill';
import Circle from 'ol/style/circle';
import proj from 'ol/proj';
import extent from 'ol/extent';
import control from 'ol/control';
import loadingstrategy from 'ol/loadingstrategy';
export default {
  data() {
    return {
      geom1: "POLYGON((100.02715479879 32.168082192159,102.76873121104 37.194305614622,107.0334056301 34.909658604412,105.96723702534 30.949603786713,100.02715479879 32.168082192159))",
      distance: 0.5,
      geom:'',
      map: null,
      source:null
    }
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
        center: proj.fromLonLat([110, 39])
      });

      let source = new Vector({
        features: []
      });
      let vector = new VectorLayer({
        source: source,
        style: styleFunction
      });
      let map = new Map({
        controls: control.defaults({
          attribution: false
        }).extend([]),
        target: "map",
        layers: [
          getTdtLayer('vec_w'),
          getTdtLayer('cva_w'),
          vector
        ],
        view: view
      });
      this.source = source
      this.map = map
    },
    async analysis(){
      if (this.source)
        this.source.clear();
      const { data: res } = await this.$API.buffAnalysis({
          geom:this.geom1,
          distance:this.distance
        })
      if(!res.success) {
        this.geom = res.msg
      }
      this.geom = res.data
      let wkts = [this.geom1,this.geom];
      let features = [];
      let wktformat = new WKT();
      for (let i = 0; i < wkts.length; i++) {
        let feature = wktformat.readFeature(wkts[i]);
        feature.getGeometry().transform("EPSG:4326", "EPSG:3857");
        if (i === 2) feature.set("index", i);
        features.push(feature);
      }
      this.source.addFeatures(features);
    }
  }
}
</script>

<style>
#map{
  height: 1000px;
}
.el-row {
  margin-bottom: 5px;
&:last-child {
   margin-bottom: 0;
 }
}
.el-col {
  border-radius: 4px;
}
</style>
