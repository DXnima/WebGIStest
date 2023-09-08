<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>OpenLayers</el-breadcrumb-item>
      <el-breadcrumb-item>地图弹窗</el-breadcrumb-item>
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
import Map from 'ol/map';
import View from 'ol/view';
import Overlay from 'ol/overlay';
import control from 'ol/control';
import proj from 'ol/proj';
import coordinate from 'ol/coordinate';
import {getTdtLayer} from "../../utils/searchUtils";
export default {
  data() {
    return {
      overlay: null
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
      let closer = document.getElementById('popup-closer');
      let container = document.getElementById('popup');
      let overlay = new Overlay({
        element: container
      });
      this.overlay = overlay
      map.addOverlay(overlay);
      closer.onclick = function () {
        container.style.display = 'none';
        closer.blur();
        return false;
      };
      const that = this
      map.on('click', function (evt) {
        let hdms = coordinate.toStringHDMS(proj.transform(
          evt.coordinate, 'EPSG:4326', 'EPSG:4326'));
        let info = '<p>You clicked here:</p><code>' + hdms + '</code>';
        that.showInfo(evt.coordinate, info);
      });
    },
    showInfo(coordinate, info) {
      let title = document.getElementById('popup-title');
      let content = document.getElementById('popup-content');
      let container = document.getElementById('popup');
      title.innerHTML = "提示信息";
      content.innerHTML = info;
      title.style.display = "block";
      container.style.display = "block";
      this.overlay.setPosition(coordinate);
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
