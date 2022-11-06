<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>OpenLayers</el-breadcrumb-item>
      <el-breadcrumb-item>点击高亮</el-breadcrumb-item>
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
import Select from 'ol/interaction/select';
import Style from 'ol/style/style';
import Stroke from 'ol/style/stroke';
import Fill from 'ol/style/fill';
import Circle from 'ol/style/circle';
import Text from 'ol/style/text';
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
      const that = this
      let view = new View({
        zoom: 4,
        center: proj.transform([110, 39], "EPSG:4326", "EPSG:3857")
      });

      let map = new Map({
        controls: control.defaults({
          attribution: false
        }).extend([]),
        target: "map",
        layers: [
          getTdtLayer("vec_w"),
          getTdtLayer("cva_w")
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
      let vectorSource = new Vector({
        features: features
      });
      console.log(vectorSource.getUrl());
      let vector = new VectorLayer({
        source: vectorSource,
        style: function (feature) {
          return that.styleFunction(feature, false);
        }
      });
      map.addLayer(vector);

      let select = new Select({
        // condition: ol.events.condition.pointerMove,
        layers: [vector],
        multi: true,
        style: function (feature) {
          return that.styleFunction(feature, true);
        }
      });
      map.addInteraction(select);
      select.on("select", function (e) {
        console.log(e);
        let features = e.selected;
        if (features.length > 0) {
          let feature = features[0];
          console.log(feature.get("name"));
        }
      });

      map.on('pointermove', function (evt) {
        if (map.hasFeatureAtPixel(evt.pixel)) {
          map.getTargetElement().style.cursor = "pointer"
        } else {
          map.getTargetElement().style.cursor = "default"
        }
      });
    },
    styleFunction(feature, isSelect) {
      let stroke = new Stroke({
        color: 'black',
        width: 2
      });
      let fill = new Fill({
        color: 'red'
      });
      let _name = "",
        _color = "#ccc";
      if (isSelect) {
        _name = feature.get("name");
        _color = "#f00";
      }
      return new Style({
        stroke: stroke,
        fill: fill,
        image: new Circle({
          radius: 8,
          fill: new Fill({
            color: _color
          })
        }),
        text: new Text({
          text: _name,
          textAlign: "left",
          offsetX: 12,
          font: "bold 13px sans-serif",
          fill: new Fill({
            color: 'red'
          }),
          stroke: new Stroke({
            color: 'white',
            width: 2
          })
        })
      });
    }
  }

}
</script>

<style lang="less" scoped>

</style>
