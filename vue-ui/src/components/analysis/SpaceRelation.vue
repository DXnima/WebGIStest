<template>
    <div>
        <!--面包屑导航区域-->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间分析</el-breadcrumb-item>
            <el-breadcrumb-item>空间关系</el-breadcrumb-item>
        </el-breadcrumb>
        <!--卡片区域-->
        <el-card>
          <el-row>
            <el-col :span="8">
                <el-input type="textarea" :rows="4" placeholder="请输入WKT" v-model="geom1"></el-input>
            </el-col>
            <el-col :span="8">
                <el-input type="textarea" :rows="4" placeholder="请输入WKT" v-model="geom2"></el-input>
            </el-col>
            <el-col :span="6">
                <el-input type="textarea" :rows="4" placeholder="显示结果" v-model="geom"></el-input>
            </el-col>
            <el-col :span="2">
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
import VectorLayer from 'ol/layer/vector';
import Vector from 'ol/source/vector';
import WKT from 'ol/format/wkt';
import proj from 'ol/proj';
import control from 'ol/control';
export default {
  data() {
    return {
      geom1: "POLYGON((100.02715479879 32.168082192159,102.76873121104 37.194305614622,107.0334056301 34.909658604412,105.96723702534 30.949603786713,100.02715479879 32.168082192159))",
      geom2:"POLYGON((96.219409781775 32.777321394882,96.219409781775 40.240501628236,104.82491352023001 40.240501628236,104.82491352023001 32.777321394882,96.219409781775 32.777321394882))",
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
      const {data:res} = await this.$http.get("spa/relation",{params:{
          geom1:this.geom1,
          geom2:this.geom2
        }})
      if(!res.success) {
        this.geom = res.msg
      }
      this.geom = JSON.stringify(res.data, null, 4)
      let wkts = [this.geom1,this.geom2];
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
