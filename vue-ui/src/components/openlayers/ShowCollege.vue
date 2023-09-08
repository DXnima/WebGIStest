<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>OpenLayers</el-breadcrumb-item>
      <el-breadcrumb-item>综合例子</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <div id="map">
        <div class="query-box">
          <table cellspacing="0" cellpadding="0">
            <tr>
              <th width=40>名称</th>
              <td>
                <input id="name" type="text" placeholder="请输入大学名称..." style="width: 185px;">
                <input id="query" type="button" value="查询" style="float: right;" @click="queryClick">
              </td>
            </tr>
            <tr>
              <th>所属地</th>
              <td class="province">
                <span @click="spanClick1($event)">全部</span>
                <span @click="spanClick1($event)">北京</span>
                <span @click="spanClick1($event)">天津</span>
                <span @click="spanClick1($event)">河北</span>
                <span @click="spanClick1($event)">山西</span>
                <span @click="spanClick1($event)">内蒙古</span>
                <span @click="spanClick1($event)">辽宁</span>
                <span @click="spanClick1($event)">吉林</span>
                <span @click="spanClick1($event)">黑龙江</span>
                <span @click="spanClick1($event)">上海</span>
                <span @click="spanClick1($event)">江苏</span>
                <span @click="spanClick1($event)">安徽</span>
                <span @click="spanClick1($event)">浙江</span>
                <span @click="spanClick1($event)">福建</span>
                <span @click="spanClick1($event)">江西</span>
                <span @click="spanClick1($event)">山东</span>
                <span @click="spanClick1($event)">河南</span>
                <span class="active" @click="spanClick1($event)">湖北</span>
                <span @click="spanClick1($event)">湖南</span>
                <span @click="spanClick1($event)">广东</span>
                <span @click="spanClick1($event)">广西</span>
                <span @click="spanClick1($event)">海南</span>
                <span @click="spanClick1($event)">重庆</span>
                <span @click="spanClick1($event)">四川</span>
                <span @click="spanClick1($event)">贵州</span>
                <span @click="spanClick1($event)">云南</span>
                <span @click="spanClick1($event)">陕西</span>
                <span @click="spanClick1($event)">西藏</span>
                <span @click="spanClick1($event)">甘肃</span>
                <span @click="spanClick1($event)">青海</span>
                <span @click="spanClick1($event)">宁夏</span>
                <span @click="spanClick1($event)">新疆</span>
              </td>
            </tr>
            <tr>
              <th>等级</th>
              <td class="level">
                <span class="active" @click="spanClick2($event)">全部</span>
                <span @click="spanClick2($event)">本科</span>
                <span @click="spanClick2($event)">高职(专科)</span>
              </td>
            </tr>
            <tr>
              <th>类型</th>
              <td class="isprivate">
                <span class="active" @click="spanClick3($event)">全部</span>
                <span @click="spanClick3($event)">综合</span>
                <span @click="spanClick3($event)">工科</span>
                <span @click="spanClick3($event)">农业</span>
                <span @click="spanClick3($event)">林业</span>
                <span @click="spanClick3($event)">医药</span>
                <span @click="spanClick3($event)">师范</span>
                <span @click="spanClick3($event)">语言</span>
                <span @click="spanClick3($event)">财经</span>
                <span @click="spanClick3($event)">政法</span>
                <span @click="spanClick3($event)">体育</span>
                <span @click="spanClick3($event)">艺术</span>
                <span @click="spanClick3($event)">民族</span>
              </td>
            </tr>
          </table>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script>
import Map from 'ol/map';
import View from 'ol/view';
import Feature from 'ol/feature';
import Point from 'ol/geom/point';
import VectorLayer from 'ol/layer/vector';
import Heatmap from 'ol/layer/heatmap';
import Vector from 'ol/source/vector';
import Style from 'ol/style/style';
import Stroke from 'ol/style/stroke';
import Fill from 'ol/style/fill';
import Circle from 'ol/style/circle';
import Text from 'ol/style/text';
import proj from 'ol/proj';
import extent from 'ol/extent';
import control from 'ol/control';
import {getTdtLayer} from "../../utils/searchUtils";
import {getUniversity} from "@/api";
export default {
  data() {
    return {
      map: null,
      source: null
    }
  },
  created() {
  },
  mounted() {
    this.initMap()
  },
  methods: {
    initMap() {
      let base = getTdtLayer("img_w")

      let source = new Vector({
        features: []
      });

      let layer1 = new Heatmap({
        source: source,
        blur: 20,
        radius: 8,
        opacity: .75,
        shadow: 500
      });

      let layer2 = new VectorLayer({
        source: source,
        style: function (feat) {
          let name = map.getView().getZoom() > 10 ? feat.get("attr").name : "";
          return new Style({
            image: new Circle({
              radius: 4,
              fill: new Fill({
                color: '#5367ec'
              })
            }),
            text: new Text({
              text: name,
              fill: new Fill({
                color: '#ffffff'
              }),
              textAlign: "left",
              offsetX: "6",
              stroke: new Stroke({
                color: '#d7d7d7',
                width: 1
              })
            })
          })
        }
      });

      let map = new Map({
        controls: control.defaults({
          attribution: false
        }),
        target: 'map',
        layers: [base, layer1, layer2],
        view: new View({
          center: proj.fromLonLat([98.633, 31.607]),
          zoom: 4,
          minZoom: 0,
          maxZoom: 18
        })
      });
      this.source = source
      this.map = map
      this.showUniversity();
    },
    async showUniversity() {
      let name = document.getElementById("name").value
      let level = document.querySelector("td.level").querySelector("span.active").innerHTML
      let province = document.querySelector("td.province").querySelector("span.active").innerHTML
      let isprivate = document.querySelector("td.isprivate").querySelector("span.active").innerHTML
      level = level === "全部" ? "" : level;
      province = province === "全部" ? "" : province;
      isprivate = isprivate === "全部" ? "" : isprivate;

      let paras = {
        name: name,
        level: level,
        province: province,
        type: isprivate
      };
      const { data: result } = await this.$API.getUniversity(paras)
      if (this.source)
        this.source.clear();
      let features = [], coords = []
      for (let i = 0; i < result.data.length; i++) {
        let _r = result.data[i];
        let coord = proj.fromLonLat([_r.lon, _r.lat]);
        coords.push(coord)
        features.push(new Feature({
          geometry: new Point(coord),
          attr: _r
        }))
      }
      this.source.addFeatures(features);
      //获取一个坐标数组的边界，格式为[minx,miny,maxx,maxy]
      if (coords.length > 0) {
        let ex = extent.boundingExtent(coords);
        //获取边界区域的中心位置
        let center = extent.getCenter(ex);
        //设置当前地图的显示中心位置
        this.map.getView().setCenter(center);
      }
    },
    spanClick1(event) {
      let docm = event.target
      let child = document.querySelector("td.province").children
      this.removeActive(child)
      docm.classList.add('active')
      this.showUniversity()
    },
    spanClick2(event) {
      let docm = event.target
      let child = document.querySelector("td.level").children
      this.removeActive(child)
      docm.classList.add('active')
      this.showUniversity()
    },
    spanClick3(event) {
      let docm = event.target
      let child = document.querySelector("td.isprivate").children
      this.removeActive(child)
      docm.classList.add('active')
      this.showUniversity()
    },
    removeActive(child) {
      for (let i = 0; i < child.length; i++) {
        const element = child[i];
        element.classList.remove('active')
      }
    },
    queryClick() {
      this.showUniversity()
    }
  }
}
</script>

<style>
.query-box {
  font-size: 12px;
  position: absolute;
  top: 140px;
  right: 50px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 1px 1px 2px #ccc;
  padding: 6px;
  z-index: 9;
  width: 300px;
}

.query-box table {
  width: 100%;
}

.query-box table th,
.query-box table td {
  padding: 5px;
  text-align: left;
}

.query-box table th span,
.query-box table td span,
.span_class {
  text-decoration: underline;
  display: inline-block;
  padding: 3px 5px;
  cursor: pointer;
}

.query-box table th span.active,
.query-box table td span.active {
  background-color: #da6300;
  color: white;
}

.query-box table th {
  text-align: right;
}
</style>
