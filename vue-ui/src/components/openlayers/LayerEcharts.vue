<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>OpenLayers</el-breadcrumb-item>
      <el-breadcrumb-item>结合Echarts</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <div id="map">
        <div id="popup" class="ol-popup">
          <a href="#" id="popup-closer" class="ol-popup-closer"></a>
          <div id="popup-title" class="popup-title"></div>
          <div id="popup-content" class="popup-content"></div>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script>
import echarts from "echarts"
import Map from 'ol/map';
import View from 'ol/view';
import Overlay from 'ol/overlay';
import proj from 'ol/proj';
import control from 'ol/control';
import {getTdtLayer} from "../../utils/searchUtils";
export default {
  data() {
    return {
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
      this.map = map
      setTimeout(this.addMapChart, 1000);
    },
    addMapChart() {
      let chartData = [
        {
          "name": "乌鲁木齐",
          "x": 87.5758285931,
          "y": 43.782211646,
          "data": [
            {
              "name": "男",
              "value": 40
            },
            {
              "name": "女",
              "value": 60
            }
          ]
        },
        {
          "name": "拉萨",
          "x": 91.162997504,
          "y": 29.7104204643,
          "data": [
            {
              "name": "男",
              "value": 45
            },
            {
              "name": "女",
              "value": 55
            }
          ]
        },
        {
          "name": "北京",
          "x": 116.4575803581078,
          "y": 40.04054437977018,
          "data": [
            {
              "name": "男",
              "value": 35
            },
            {
              "name": "女",
              "value": 65
            }
          ]
        },
        {
          "name": "兰州",
          "x": 103.584297498,
          "y": 36.1190864503,
          "data": [
            {
              "name": "男",
              "value": 44
            },
            {
              "name": "女",
              "value": 56
            }
          ]
        }
      ];
      for (let i = 0, len = chartData.length; i < len; i++) {
        let _d = chartData[i];
        let _coord = proj.fromLonLat([_d.x, _d.y]);
        let _dom = document.createElement("div");
        _dom.style.position = "absolute"
        _dom.style.width = "40px"
        _dom.style.height = "40px"
        let _overlay = new Overlay({
          element: _dom,
          positioning: "center-center",
          position: _coord
        });
        this.map.addOverlay(_overlay);
        this.addChart(_dom, _d.data);
      }
    },
    addChart(dom, data) {
      let chart = echarts.init(dom);
      let option = {
        // renderAsImage:true,
        animation: true,
        tooltip: {
          trigger: 'item',
          formatter: "{b}:{c}"
        },
        series: [
          {
            type: 'pie',
            radius: '100%',
            center: ['50%', '50%'],
            itemStyle: {
              normal: {
                labelLine: { show: false }
              }
            },
            data: data,
            line: false
          }
        ]
      };
      chart.setOption(option);
    }
  }
}
</script>

<style lang="less" scoped>
.ol-popup {
  display: none;
  position: absolute;
  background-color: white;
  -moz-box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
  -webkit-filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
  filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
  border: 1px solid #cccccc;
  bottom: 12px;
  left: -100px;
  width: 200px;
}

.ol-popup:after,
.ol-popup:before {
  top: 100%;
  border: solid transparent;
  content: " ";
  height: 0;
  width: 0;
  position: absolute;
  pointer-events: none;
}

.ol-popup:after {
  border-top-color: white;
  border-width: 10px;
  left: 100px;
  margin-left: -10px;
}

.ol-popup:before {
  border-top-color: #cccccc;
  border-width: 11px;
  left: 100px;
  margin-left: -11px;
}

.ol-popup .popup-title {
  font-weight: bold;
  border-bottom: 1px solid #cccccc;
  padding: 5px 8px;
}

.ol-popup .popup-content {
  padding: 5px 8px;
}

.ol-popup .ol-popup-closer {
  text-decoration: none;
  position: absolute;
  top: 6px;
  right: 6px;
}

.ol-popup .ol-popup-closer:after {
  content: "×";
}
</style>
