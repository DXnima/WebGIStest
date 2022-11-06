<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>OpenLayers</el-breadcrumb-item>
      <el-breadcrumb-item>加载后端图层</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <div id="map"></div>
    </el-card>
  </div>
</template>
<script>
import Map from 'ol/map';
import View from 'ol/view';
import Feature from 'ol/feature';
import Point from 'ol/geom/point';
import VectorLayer from 'ol/layer/vector';
import Vector from 'ol/source/vector';
import Style from 'ol/style/style';
import Stroke from 'ol/style/stroke';
import Fill from 'ol/style/fill';
import RegularShape from 'ol/style/regularshape';
import proj from 'ol/proj';
import control from 'ol/control';
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
    async initMap() {
      let view = new View({
        zoom: 4,
        center: proj.transform([110, 39], "EPSG:4326", "EPSG:3857")
      });

      let vector = new VectorLayer({
        source: null,
        style: this.styleFunction
      });

      let map = new Map({
        controls: control.defaults({
          attribution: false
        }).extend([]),
        target: "map",
        layers: [
          getTdtLayer("vec_w"),
          getTdtLayer("cva_w"),
          vector
        ],
        view: view
      });
      const { data: result } = await this.$API.getCapital()
      let features = [];
      for (let i = 0, len = result.data.length; i < len; i++) {
        let _r = result.data[i],
          coord = proj.transform([_r.lon, _r.lat], "EPSG:4326", "EPSG:3857");
        let feature = new Feature({
          geometry: new Point(coord),
          name: _r.name
        });
        features.push(feature);
      }
      let source = new Vector({
        features: features
      });
      vector.setSource(source);
      map.on('pointermove', function (evt) {
        if (map.hasFeatureAtPixel(evt.pixel)) {
          map.getTargetElement().style.cursor = "pointer"
          map.forEachFeatureAtPixel(evt.pixel, function (features) {
            console.log(features);
            let name = features.get("name");
            console.log(name);
          });
        } else {
          map.getTargetElement().style.cursor = "default"
        }
      });
    },
    styleFunction(feature) {
      let stroke = new Stroke({
        color: 'black',
        width: 2
      });
      let fill = new Fill({
        color: 'red'
      });
      let _name = feature.get("name");
      let _radius = 6,
        _radius2 = 6;
      if (_name === "北京") {
        _radius = 8;
        _radius2 = 4;
      }
      return new Style({
        stroke: stroke,
        fill: fill,
        image: new RegularShape({
          fill: fill,
          stroke: stroke,
          points: 5,
          radius: _radius,
          radius2: _radius2,
          angle: 0
        })
      });
    }
  }
}
</script>

<style lang="less" scoped>

</style>
