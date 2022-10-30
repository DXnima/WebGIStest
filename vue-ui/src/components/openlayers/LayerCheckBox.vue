<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>OpenLayers</el-breadcrumb-item>
      <el-breadcrumb-item>图层多选控件</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <div id="map" style="width:100%;height:100%">
        <div id="mouse-position"></div>
        <div id="layerControl" class="layerControl">
          <div class="title"><label>图层列表</label></div>
          <ul id="layerTree" class="layerTree"></ul>
        </div>
      </div>
    </el-card>
  </div>

</template>
<script>
import Map from 'ol/map';
import View from 'ol/view';
import TileLayer from 'ol/layer/tile';
import ImageLayer from 'ol/layer/image';
import TileGrid from 'ol/tilegrid/tilegrid';
import WMTSGrid from 'ol/tilegrid/wmts';
import OSM from "ol/source/osm";
import XYZ from 'ol/source/xyz';
import TileImage from 'ol/source/tileimage';
import ImageWMS from 'ol/source/imagewms';
import TileWMS from 'ol/source/tilewms';
import WMTS from 'ol/source/wmts';
import proj from 'ol/proj';
import extent from 'ol/extent';
export default {
  data() {
    return {
    }
  },
  created() {
  },
  mounted() {
    this.initMap()
  },
  methods: {
    initMap() {

      //实例化Map对象加载地图
      let map = new Map({
        target: 'map', //地图容器div的ID
        //地图容器中加载的图层
        layers: [this.osmMap(), this.baiduMap(), this.tianMap(), this.wmsImage(), this.wmsTile(), this.wmsGrid(), this.wmtsMap()],
        //地图视图设置
        view: new View({
          center: [0, 0], //地图初始中心点
          zoom: 2 //地图初始显示级别
        })
      });
      console.log(map);
      //加载图层列表数据
      this.loadLayersControl(map, "layerTree");
    },
    //OSM地图
    osmMap() {

      const openStreetMap = new TileLayer({
        source: new OSM(),
        visible: true,
        name: 'OSM地图'
      })
      return openStreetMap;
    },
    //百度地图
    baiduMap() {

      let projection = proj.get("EPSG:3857");
      let resolutions = [];
      for (let i = 0; i < 19; i++) {
        resolutions[i] = Math.pow(2, 18 - i);
      }
      let tilegrid = new TileGrid({
        origin: [0, 0],
        resolutions: resolutions
      });

      let baidu_source = new TileImage({
        projection: projection,
        tileGrid: tilegrid,
        tileUrlFunction: function (tileCoord, pixelRatio, proj) {
          if (!tileCoord) {
            return '';
          }
          let z = tileCoord[0];
          let x = tileCoord[1];
          let y = -tileCoord[2] - 1;
          return "http://online1.map.bdimg.com/onlinelabel/?qt=tile&x=" + x + "&y=" + y + "&z=" + z + "&styles=pl&scaler=1&udt=20191119";
        },
      });

      const baiduMap = new TileLayer({
        source: baidu_source,
        visible: true,
        name: '百度地图'
      })
      return baiduMap;
    },
    //天地图
    tianMap() {

      const tianMap = new TileLayer({
        source: new XYZ({
          url: "http://t4.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}&tk=49ea1deec0ffd88ef13a3f69987e9a63"
        }),
        visible: true,
        name: '天地图'
      })
      return tianMap;
    },
    //wmsImage服务
    wmsImage() {

      let wmsImageSource = new ImageWMS({
        url: 'https://ahocelet.com/geoserver/wms', //WMS服务基地址
        params: { 'LAYERS': 'topp:states' }, //图层参数
        ratio: 1,
        serverType: 'geoserver', //服务类型
      });
      return new ImageLayer({
        extent: [-13884991, 2870341, -7455066, 6338219], //数据范围
        source: wmsImageSource,
        visible: true,
        name: 'wmsImage地图'
      });
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
        visible: true,
        name: 'wmsTile地图'
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
      //实例化ol.tilegrid.TileGrid对象
      let tileGrid = new TileGrid({
        extent: [-13884991, 2870341, -7455066, 6338219], //数据范围
        resolutions: resolutions, //分辨率数组
        tileSize: [512, 256] //瓦片大小
      });
      //使用TileLayer实例化WMS图层对象，设置ol.source.TileWMS的tileGrid参数
      let wmsGridSource = new TileWMS({
        url: 'https://ahocelet.com/geoserver/wms', //WMS服务地址
        params: { 'LAYERS': 'topp:states', 'TILED': true }, //图层等参数
        serverType: 'geoserver', //服务类型
        tileGrid: tileGrid  //瓦片网格对象参数（瓦片大小为512x256）
      })

      return new TileLayer({
        source: wmsGridSource,
        visible: true,
        name: 'wmsGrid地图'
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
      //实例化WMTS服务图层对象（TileLayer，ol.source.WMTS）
      return new TileLayer({
        opacity: 0.7, //图层透明度
        source: wmtsSource,
        visible: true,
        name: 'wmtsMap地图'
      });
    },

    /**
     * 加载图层列表数据
     * @param {ol.Map} map 地图对象
     * @param {string} id 图层列表容器ID
     */
    loadLayersControl(map, id) {
      let layer = new Array();  //map中的图层数组
      let layerName = new Array();  //图层名称数组
      let layerVisibility = new Array();  //图层可见属性数组
      let treeContent = document.getElementById(id); //图层目录容器
      let layers = map.getLayers(); //获取地图中所有图层
      for (let i = 0; i < layers.getLength(); i++) {
        //获取每个图层的名称、是否可见属性
        layer[i] = layers.item(i);
        layerName[i] = layer[i].get('name');
        layerVisibility[i] = layer[i].getVisible();

        //新增li元素，用来承载图层项
        let elementLi = document.createElement('li');
        treeContent.appendChild(elementLi); // 添加子节点
        //创建复选框元素
        let elementInput = document.createElement('input');
        elementInput.type = "checkbox";
        elementInput.name = "layers";
        elementLi.appendChild(elementInput);
        //创建label元素
        let elementLable = document.createElement('label');
        elementLable.className = "layer";
        //设置图层名称
        this.setInnerText(elementLable, layerName[i]);
        elementLi.appendChild(elementLable);

        //设置图层默认显示状态
        if (layerVisibility[i]) {
          elementInput.checked = true;
        }
        this.addChangeEvent(elementInput, layer[i]);  //为checkbox添加变更事件
      }
    },
    /**
     * 为checkbox元素绑定变更事件
     * @param {input} element checkbox元素
     * @param {ol.layer.Layer} layer 图层对象
     */
    addChangeEvent(element, layer) {
      element.onclick = function () {
        if (element.checked) {
          layer.setVisible(true); //显示图层
        } else {
          layer.setVisible(false); //不显示图层
        }
      };
    },
    /**
     * 动态设置元素文本内容（兼容）
     */
    setInnerText(element, text) {
      if (typeof element.textContent == "string") {
        element.textContent = text;
      } else {
        element.innerText = text;
      }
    }
  }
}
</script>

<style lang="less" scoped>
/* 图层控件层样式设置 */
.layerControl {
  position: absolute;
  bottom: 5px;
  min-width: 200px;
  max-height: 250px;
  right: 40px;
  top: 130px;
  z-index: 2001;
  /*在地图容器中的层，要设置z-index的值让其显示在地图上层*/
  color: #ffffff;
  background-color: #4c4e5a;
  border-width: 10px;
  /*边缘的宽度*/
  border-radius: 10px;
  /*圆角的大小 */
  border-color: #000 #000 #000 #000;
  /*边框颜色*/
}

.layerControl .title {
  font-weight: bold;
  font-size: 15px;
  margin: 10px;
}

.layerTree li {
  list-style: none;
  margin: 5px 10px;
}

/* 鼠标位置控件层样式设置 */
#mouse-position {
  float: left;
  position: absolute;
  bottom: 5px;
  width: 200px;
  height: 20px;
  z-index: 2000;
  /*在地图容器中的层，要设置z-index的值让其显示在地图上层*/
}
</style>
