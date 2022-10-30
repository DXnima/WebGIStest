<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>OpenLayers</el-breadcrumb-item>
      <el-breadcrumb-item>图层切换</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <el-container>
        <el-aside width="200px">
          <h2>切换图层</h2>
          <el-radio-group v-model="radio" size="medium" @change="radioChange">
            <el-radio label="osm">OSM地图</el-radio><br>
            <el-radio label="baidu">百度地图</el-radio><br>
            <el-radio label="tian">天地图</el-radio><br>
            <el-radio label="wmsImage">WMS Image服务</el-radio><br>
            <el-radio label="wmsTile">WMS Tile服务</el-radio><br>
            <el-radio label="wmsGrid">WMS Grid服务</el-radio><br>
            <el-radio label="wmtsMap">WMTS服务</el-radio><br>
            <el-radio label="wfsMap">WFS服务</el-radio><br>
          </el-radio-group>
        </el-aside>
        <el-main>
          <div id="map" style="width:100%;height:100%"></div>
        </el-main>
      </el-container>
    </el-card>
  </div>
</template>
<script>
import Map from 'ol/map';
import View from 'ol/view';
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
import Style from 'ol/style/style';
import Stroke from 'ol/style/stroke';
import proj from 'ol/proj';
import extent from 'ol/extent';
import loadingstrategy from 'ol/loadingstrategy';
export default {
  data() {
    return {
      radio: 'osm',
      map: null,
      layerGroup: null
    }
  },
  created() {

  },
  mounted() {
    this.initMap()
  },
  methods: {
    initMap() {
      const map = new Map({
        view: new View({
          projection: "EPSG:3857",
          center: [12955655.890681803, 4849776.570637863],
          zoom: 5
        }),
        target: 'map'
      })
      //图层组
      let layerGroup = new GroupLayer({
        layers: [
          this.osmMap(),
          this.baiduMap(),
          this.tianMap("vec_w"),
          this.tianMap("cva_w"),
          this.wmsImage(),
          this.wmsTile(),
          this.wmsGrid(),
          this.wmtsMap(),
          this.wfsRaster(),
          this.wfsVector()
        ]
      })
      //添加图层
      map.addLayer(layerGroup);
      map.on("pointermove", function (evt) {
        let lonlat = proj.transform(evt.coordinate, "EPSG:3857", "EPSG:4326");
        // console.log(evt.coordinate)
      });
      this.layerGroup = layerGroup
      this.map = map
    },
    //单选框事件
    radioChange(val) {
      const that = this
      this.layerGroup.getLayers().forEach(function (element, index, array) {
        let title = element.get('title');
        element.setVisible(title === val);
        if (title === val) {
          let view
          switch (title) {
            case 'baidu':
            case 'tian':
              view = new View({
                projection: "EPSG:3857",
                center: [12955655.890681803, 4849776.570637863],
                zoom: 8
              })
              break
            default:
              view = new View({
                center: [12959773, 4853101],
                zoom: 5
              })
              break
          }
          that.map.setView(view)
        }
      })
    },
    //OSM地图
    osmMap() {
      return new TileLayer({
        source: new OSM(),
        visible: true,
        title: 'osm'
      });
    },
    //百度地图
    baiduMap() {
      let resolutions = [];
      let baiduX, baiduY;

      for (let i = 0; i < 19; i++) {
        resolutions[i] = Math.pow(2, 18 - i);
      }
      let tilegrid = new TileGrid({
        origin: [0, 0],
        resolutions: resolutions
      });

      let baidu_source = new TileImage({
        projection: "EPSG:3857",
        tileGrid: tilegrid,
        tileUrlFunction: function (tileCoord) {
          if (!tileCoord) return "";
          let z = tileCoord[0];
          let x = tileCoord[1];
          let y = tileCoord[2];
          // 对编号xy处理
          baiduX = (x < 0) ? x : 'M' + (-x);
          baiduY = -y;
          return "http://online3.map.bdimg.com/onlinelabel/?qt=tile&x=" + baiduX + "&y=" + baiduY + "&z=" + z + "&styles=pl&udt=20151021&scaler=1&p=1";
        }
      });
      return new TileLayer({
        source: baidu_source,
        visible: false,
        title: 'baidu'
      })
    },
    //天地图
    tianMap(lyr) {
      return new TileLayer({
        source: new XYZ({
          url: "http://t0.tianditu.com/DataServer?T=" + lyr + "&X={x}&Y={y}&L={z}&tk=16c5722fed64bcdbb390cc21a5548cf9"
        }),
        visible: false,
        title: 'tian'
      });
    },
    //wmsImage服务
    wmsImage() {
      return new ImageLayer({
        extent: [-13884991, 2870341, -7455066, 6338219],
        source: new ImageWMS({
          url: 'https://ahocevar.com/geoserver/wms',//WMS服务基地址
          params: { 'LAYERS': 'topp:states' },//图层参数
          ratio: 1,
          serverType: 'geoserver',//服务类型
        }),
        visible: false,
        title: 'wmsImage'
      })
    },
    //wmsTile服务
    wmsTile() {
      let wmsTileSource = new TileWMS({
        url: 'https://ahocelet.com/geoserver/wms', //WMS服务地址
        params: { 'LAYERS': 'ne:ne_10m_admin_0_countries', 'TILED': true }, //图层等参数
        serverType: 'geoserver'//服务类型
      });
      return new TileLayer({
        source: wmsTileSource,
        visible: false,
        title: 'wmsTile'
      });
    },
    //wmsGrid服务
    wmsGrid() {
      //通过范围计算得到分辨率数组对象resolutions
      let projExtent = proj.get('EPSG:3857').getExtent();
      let startResolution = extent.getWidth(projExtent) / 256;
      let resolutions = new Array(22);
      for (let i = 0, ii = resolutions.length; i < ii; ++i) {
        resolutions[i] = startResolution / Math.pow(2, i);
      }
      //实例化TileGrid对象
      let tileGrid = new TileGrid({
        extent: [-13884991, 2870341, -7455066, 6338219], //数据范围
        resolutions: resolutions, //分辨率数组
        tileSize: [512, 256] //瓦片大小
      });
      //使用TileLayer实例化WMS图层对象，设置TileWMS的tileGrid参数
      let wmsGridSource = new TileWMS({
        url: 'https://ahocelet.com/geoserver/wms', //WMS服务地址
        params: { 'LAYERS': 'topp:states', 'TILED': true }, //图层等参数
        serverType: 'geoserver', //服务类型
        tileGrid: tileGrid  //瓦片网格对象参数（瓦片大小为512x256）
      })
      return new TileLayer({
        source: wmsGridSource,
        visible: false,
        title: 'wmsGrid'
      });
    },
    //WMTS服务
    wmtsMap() {
      //通过范围计算得到分辨率数组
      let projection = proj.get('EPSG:3857');
      let projectionExtent = projection.getExtent();
      let size = extent.getWidth(projectionExtent) / 256;
      let resolutions = new Array(14);
      let matrixIds = new Array(14);
      for (let z = 0; z < 14; ++z) {
        // generate resolutions and matrixIds arrays for this WMTS
        resolutions[z] = size / Math.pow(2, z);
        matrixIds[z] = z;
      }
      let wmtsSource = new WMTS({
        attributions: // 数据源信息
          'Tiles © <a href="https://services.arcgisonline.com/arcgis/rest/' +
          'services/Demographics/USA_Population_Density/MapServer/">ArcGIS</a>',
        url: //WMTS服务基地址
          'https://services.arcgisonline.com/arcgis/rest/' +
          'services/Demographics/USA_Population_Density/MapServer/WMTS/',
        layer: '0',
        matrixSet: 'EPSG:3857', //投影坐标系设置矩阵
        format: 'image/png', //图片格式
        projection: projection, //数据的投影坐标系
        //瓦片网格对象
        tileGrid: new WMTSGrid({
          origin: new extent.getTopLeft(projectionExtent), //原点（左上角）
          resolutions: resolutions, //分辨率数组
          matrixIds: matrixIds, //矩阵标识列表，与地图级数保持一致
        }),
        style: 'default',
        wrapX: true,
      })
      //实例化WMTS服务图层对象（TileLayer，WMTS）
      return new TileLayer({
        opacity: 0.7, //图层透明度
        source: wmtsSource,
        visible: false,
        title: 'wmtsMap'
      });
    },
    wfsVector() {
      let wfsLayer; //WFS图层数据
      let vectorSource;// 矢量图层数据源
      // 创建ol.format.GeoJSON对象，用来解析 WFS GetFeature接口的响应结果
      let geojsonFormat = new GeoJSON();
      //实例化矢量图层数据源对象（使用ajax请求WFS服务）
      vectorSource = new Vector({
        format: geojsonFormat,
        url: function (extent) {
          return (
            'https://ahocelet.com/geoserver/wfs?service=WFS&' +
            'version=1.1.0&request=GetFeature&typename=osm:water_areas&' +
            'outputFormat=application/json&srsname=EPSG:3857&' +
            'bbox=' + extent.join(',') + ',EPSG:3857'
          );
        },
        strategy: loadingstrategy.bbox(),
      });
      return new VectorLayer({
        source: vectorSource,
        style: new Style({
          stroke: new Stroke({
            color: 'rgba(0, 0, 255, 1.0)',
            width: 2,
          }),
        }),
        visible: false,
        title: 'wfsMap'
      });
    },
    wfsRaster() {
      let key = 'get_your_own_D6rA4zTHduk6KOKTXzGB';
      let attributions =
        '<a href="https://www.maptiler.com/copyright/" target="_blank">&copy; MapTiler</a> ' +
        '<a href="https://www.openstreetmap.org/copyright" target="_blank">&copy; OpenStreetMap contributors</a>';
      return new TileLayer({
        source: new XYZ({
          attributions: attributions,
          url: 'https://api.maptiler.com/tiles/satellite/{z}/{x}/{y}.jpg?key=' + key,
          maxZoom: 20
        }),
        visible: false,
        title: 'wfsMap'
      });
    }
  }
}
</script>

<style lang="less" scoped>
.sidebar {
  margin-left: 15px
}

.el-radio {
  font-size: 30px;
}

.map_class {
  position: absolute;
  width: 100%;
  height: 100%;
}
</style>
