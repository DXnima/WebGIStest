<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>OpenLayers</el-breadcrumb-item>
      <el-breadcrumb-item>加载GeoJSON</el-breadcrumb-item>
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
import VectorLayer from 'ol/layer/vector';
import Vector from 'ol/source/vector';
import GeoJSON from 'ol/format/geojson';
import Style from 'ol/style/style';
import Stroke from 'ol/style/stroke';
import Fill from 'ol/style/fill';
import Circle from 'ol/style/circle';
import Text from 'ol/style/text';
import RegularShape from 'ol/style/regularshape';
import proj from 'ol/proj';
import control from 'ol/control';
import {getTdtLayer} from "../../utils/searchUtils";
export default {
  data() {
    return {
      map: null,
      geojson: {
        "type": "FeatureCollection",
        "features": [
          { "type": "Feature", "id": 0, "properties": { "name": "乌鲁木齐", "id": 10.000000, "lon": 87.576079, "lat": 43.782225, "heat": null }, "geometry": { "type": "Point", "coordinates": [87.576079383021181, 43.782225203649105] } },
          { "type": "Feature", "id": 1, "properties": { "name": "拉萨", "id": 11.000000, "lon": 91.163218, "lat": 29.710560, "heat": null }, "geometry": { "type": "Point", "coordinates": [91.163217897611261, 29.710559728178374] } },
          { "type": "Feature", "id": 2, "properties": { "name": "西宁", "id": 12.000000, "lon": 101.797439, "lat": 36.593725, "heat": null }, "geometry": { "type": "Point", "coordinates": [101.79743904230159, 36.59372482860072] } },
          { "type": "Feature", "id": 3, "properties": { "name": "兰州", "id": 13.000000, "lon": 103.584421, "lat": 36.119175, "heat": null }, "geometry": { "type": "Point", "coordinates": [103.58442065913421, 36.11917453434188] } },
          { "type": "Feature", "id": 4, "properties": { "name": "成都", "id": 14.000000, "lon": 104.035634, "lat": 30.714315, "heat": null }, "geometry": { "type": "Point", "coordinates": [104.03563440326465, 30.714314809962257] } },
          { "type": "Feature", "id": 5, "properties": { "name": "重庆", "id": 15.000000, "lon": 106.519225, "lat": 29.479073, "heat": null }, "geometry": { "type": "Point", "coordinates": [106.51922502209845, 29.479073225153105] } },
          { "type": "Feature", "id": 6, "properties": { "name": "贵阳", "id": 16.000000, "lon": 106.668183, "lat": 26.457486, "heat": null }, "geometry": { "type": "Point", "coordinates": [106.66818338058295, 26.457485617599374] } },
          { "type": "Feature", "id": 7, "properties": { "name": "昆明", "id": 17.000000, "lon": 102.726915, "lat": 24.969568, "heat": null }, "geometry": { "type": "Point", "coordinates": [102.72691472988386, 24.969568444466436] } },
          { "type": "Feature", "id": 8, "properties": { "name": "银川", "id": 18.000000, "lon": 106.167324, "lat": 38.598593, "heat": null }, "geometry": { "type": "Point", "coordinates": [106.16732429995059, 38.598592562909062] } },
          { "type": "Feature", "id": 9, "properties": { "name": "西安", "id": 19.000000, "lon": 108.967213, "lat": 34.276221, "heat": null }, "geometry": { "type": "Point", "coordinates": [108.96721346583659, 34.276221246005349] } },
          { "type": "Feature", "id": 10, "properties": { "name": "南宁", "id": 20.000000, "lon": 108.234036, "lat": 22.748502, "heat": null }, "geometry": { "type": "Point", "coordinates": [108.23403628821811, 22.748502136254036] } },
          { "type": "Feature", "id": 11, "properties": { "name": "海口", "id": 21.000000, "lon": 110.346274, "lat": 19.970150, "heat": null }, "geometry": { "type": "Point", "coordinates": [110.34627437896368, 19.970150077576061] } },
          { "type": "Feature", "id": 12, "properties": { "name": "广州", "id": 22.000000, "lon": 113.226755, "lat": 23.183277, "heat": null }, "geometry": { "type": "Point", "coordinates": [113.22675511608018, 23.183277095857289] } },
          { "type": "Feature", "id": 13, "properties": { "name": "长沙", "id": 23.000000, "lon": 112.947996, "lat": 28.170082, "heat": null }, "geometry": { "type": "Point", "coordinates": [112.94799576419183, 28.170081880824668] } },
          { "type": "Feature", "id": 14, "properties": { "name": "南昌", "id": 24.000000, "lon": 115.893762, "lat": 28.652529, "heat": null }, "geometry": { "type": "Point", "coordinates": [115.89376225263823, 28.652528581920564] } },
          { "type": "Feature", "id": 15, "properties": { "name": "福州", "id": 25.000000, "lon": 119.246798, "lat": 26.070956, "heat": null }, "geometry": { "type": "Point", "coordinates": [119.24679821001271, 26.07095583016066] } },
          { "type": "Feature", "id": 16, "properties": { "name": "台北", "id": 26.000000, "lon": 121.503585, "lat": 25.008476, "heat": null }, "geometry": { "type": "Point", "coordinates": [121.50358485991021, 25.008475846011745] } },
          { "type": "Feature", "id": 17, "properties": { "name": "杭州", "id": 27.000000, "lon": 120.183062, "lat": 30.330742, "heat": null }, "geometry": { "type": "Point", "coordinates": [120.18306242469987, 30.330742397778341] } },
          { "type": "Feature", "id": 18, "properties": { "name": "上海", "id": 28.000000, "lon": 121.449713, "lat": 31.253514, "heat": null }, "geometry": { "type": "Point", "coordinates": [121.44971306145047, 31.253514397685105] } },
          { "type": "Feature", "id": 19, "properties": { "name": "武汉", "id": 29.000000, "lon": 114.216652, "lat": 30.579401, "heat": null }, "geometry": { "type": "Point", "coordinates": [114.21665206389655, 30.579400651160647] } },
          { "type": "Feature", "id": 20, "properties": { "name": "合肥", "id": 30.000000, "lon": 117.262334, "lat": 31.838495, "heat": null }, "geometry": { "type": "Point", "coordinates": [117.26233421444691, 31.838494863931761] } },
          { "type": "Feature", "id": 21, "properties": { "name": "南京", "id": 31.000000, "lon": 118.805714, "lat": 32.085164, "heat": null }, "geometry": { "type": "Point", "coordinates": [118.80571398101925, 32.085164185755289] } },
          { "type": "Feature", "id": 22, "properties": { "name": "郑州", "id": 32.000000, "lon": 113.651151, "lat": 34.746419, "heat": null }, "geometry": { "type": "Point", "coordinates": [113.65115064151188, 34.746419209304378] } },
          { "type": "Feature", "id": 23, "properties": { "name": "济南", "id": 33.000000, "lon": 117.048354, "lat": 36.608511, "heat": null }, "geometry": { "type": "Point", "coordinates": [117.04835397414672, 36.608510842637969] } },
          { "type": "Feature", "id": 24, "properties": { "name": "石家庄", "id": 34.000000, "lon": 114.478253, "lat": 38.033361, "heat": null }, "geometry": { "type": "Point", "coordinates": [114.47825266281612, 38.033361247307013] } },
          { "type": "Feature", "id": 25, "properties": { "name": "太原", "id": 35.000000, "lon": 112.483119, "lat": 37.798488, "heat": null }, "geometry": { "type": "Point", "coordinates": [112.4831190608968, 37.798487801001173] } },
          { "type": "Feature", "id": 26, "properties": { "name": "呼和浩特", "id": 36.000000, "lon": 111.842856, "lat": 40.895807, "heat": null }, "geometry": { "type": "Point", "coordinates": [111.84285564844028, 40.895806530184856] } },
          { "type": "Feature", "id": 27, "properties": { "name": "天津", "id": 37.000000, "lon": 117.351108, "lat": 38.925801, "heat": null }, "geometry": { "type": "Point", "coordinates": [117.35110819484493, 38.925800640164205] } },
          { "type": "Feature", "id": 28, "properties": { "name": "沈阳", "id": 38.000000, "lon": 123.296260, "lat": 41.801674, "heat": null }, "geometry": { "type": "Point", "coordinates": [123.29626003649942, 41.801674462394054] } },
          { "type": "Feature", "id": 29, "properties": { "name": "长春", "id": 39.000000, "lon": 125.261357, "lat": 43.982041, "heat": null }, "geometry": { "type": "Point", "coordinates": [125.26135735800531, 43.982040844340744] } },
          { "type": "Feature", "id": 30, "properties": { "name": "哈尔滨", "id": 40.000000, "lon": 126.567056, "lat": 45.693857, "heat": null }, "geometry": { "type": "Point", "coordinates": [126.56705607814561, 45.693856553844213] } },
          { "type": "Feature", "id": 31, "properties": { "name": "北京", "id": 41.000000, "lon": 116.068297, "lat": 39.892297, "heat": null }, "geometry": { "type": "Point", "coordinates": [116.06829681327378, 39.892296888653789] } },
          { "type": "Feature", "id": 32, "properties": { "name": "香港", "id": 42.000000, "lon": 114.093184, "lat": 22.428066, "heat": null }, "geometry": { "type": "Point", "coordinates": [114.09318425169782, 22.428065990916085] } },
          { "type": "Feature", "id": 33, "properties": { "name": "澳门", "id": 43.000000, "lon": 113.552554, "lat": 22.184710, "heat": null }, "geometry": { "type": "Point", "coordinates": [113.55255358053526, 22.184709710970235] } }
        ]
      }
    }
  },
  created() {
  },
  mounted() {
    this.initMap()
  },
  methods: {
    initMap() {
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
      let features = (new GeoJSON()).readFeatures(this.geojson);
      for (let i = 0; i < features.length; i++) {
        let _geom = features[i].getGeometry();
        _geom.transform("EPSG:4326", "EPSG:3857");
      }
      let vectorSource = new Vector({
        features: features
      });
      let vector = new VectorLayer({
        source: vectorSource,
        style: that.styleFunction,
        renderMode: "vector"
      });
      map.addLayer(vector);
      this.map = map
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
      _name = this.map.getView().getZoom() > 6 ? _name : "";
      let _radius = 8,
        _radius2 = 8;
      if (_name === "北京") {
        _radius = 12;
        _radius2 = 6;
      }
      let styles = [];
      styles.push(new Style({
        stroke: stroke,
        fill: fill,
        image: new RegularShape({
          fill: fill,
          stroke: stroke,
          points: 5,
          radius: _radius,
          radius2: _radius2,
          angle: 0
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
      }));
      styles.push(new Style({
        geometry: feature.getGeometry(),
        image: new Circle({
          radius: 4,
          fill: new Fill({
            color: "white"
          })
        })
      }));

      return styles;
    }
  }
}
</script>

<style lang="less" scoped>

</style>
